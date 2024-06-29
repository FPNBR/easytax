package br.com.easytax.easytax.service;

import br.com.easytax.easytax.domain.Client;
import br.com.easytax.easytax.domain.RefreshToken;
import br.com.easytax.easytax.domain.Role;
import br.com.easytax.easytax.repository.RefreshTokenRepository;
import br.com.easytax.easytax.repository.ClientRepository;
import br.com.easytax.easytax.dto.request.LoginRequestDTO;
import br.com.easytax.easytax.dto.request.RefreshTokenRequestDTO;
import br.com.easytax.easytax.dto.response.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final JwtEncoder jwtEncoder;
    private final ClientRepository clientRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.accessExpiration}")
    private Long accessExpiration;

    @Value("${jwt.refreshExpiration}")
    private Long refreshExpiration;

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        var user = validateUserCredentials(loginRequestDTO);
        var accessToken = createAccessToken(user);
        var refreshToken = createRefreshToken(user);

        return new LoginResponseDTO(accessToken, refreshToken, accessExpiration);
    }

    public void logout(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        var refreshToken = refreshTokenRepository.findByToken(refreshTokenRequestDTO.getRefreshToken());

        if (refreshToken.isEmpty() || refreshToken.get().getExpiryDate().isBefore(Instant.now())) {
            throw new BadCredentialsException("Refresh token inválido ou expirado");
        }

        Client client = refreshToken.get().getClient();
        client.setRefreshToken(null);
        clientRepository.save(client);
        refreshToken.ifPresent(refreshTokenRepository::delete);
    }

    public LoginResponseDTO refresh(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        var refreshToken = refreshTokenRepository.findByToken(refreshTokenRequestDTO.getRefreshToken());

        if (refreshToken.isEmpty() || refreshToken.get().getExpiryDate().isBefore(Instant.now())) {
            throw new BadCredentialsException("Refresh token inválido ou expirado");
        }

        var user = refreshToken.get().getClient();
        var jwtToken = createAccessToken(user);
        var newRefreshToken = createRefreshToken(user);

        return new LoginResponseDTO(jwtToken, newRefreshToken, refreshExpiration);
    }

    private String createAccessToken(Client client) {
        var now = Instant.now();
        var expiresIn = accessExpiration;

        var roles = client.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        var claims = JwtClaimsSet.builder()
                .issuer("easytax")
                .subject(client.getCpf())
                .claim("roles", roles)
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String createRefreshToken(Client client) {
        var encodedToken = encodeRefreshToken();

        var refreshToken = refreshTokenRepository.findByClient(client)
                .orElseGet(() -> {
                    RefreshToken newRefreshToken = new RefreshToken();
                    newRefreshToken.setClient(client);
                    return newRefreshToken;
                });

        refreshToken.setToken(encodedToken);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshExpiration));

        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    public String encodeRefreshToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private Client validateUserCredentials(LoginRequestDTO loginRequestDTO) {
        return clientRepository.findByCpf(loginRequestDTO.getCpf())
                .filter(user -> user.isCredentialsValid(loginRequestDTO, bCryptPasswordEncoder))
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos"));
    }
}
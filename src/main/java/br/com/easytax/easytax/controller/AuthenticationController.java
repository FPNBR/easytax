package br.com.easytax.easytax.controller;

import br.com.easytax.easytax.dto.request.LoginRequestDTO;
import br.com.easytax.easytax.dto.request.RefreshTokenRequestDTO;
import br.com.easytax.easytax.dto.response.LoginResponseDTO;
import br.com.easytax.easytax.service.AuthenticationService;
import br.com.easytax.easytax.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Autenticação", description = "Endpoint responsável por realizar operações de autenticação")
@RequestMapping("/oauth2")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/token")
    public ApiResponse<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authenticationService.login(loginRequestDTO);

        return new ApiResponse<>(true, "Login realizado com sucesso", loginResponseDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        authenticationService.logout(refreshTokenRequestDTO);

        return new ApiResponse<>(true, "Logout realizado com sucesso", null);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/refresh")
    public ApiResponse<LoginResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        LoginResponseDTO loginResponseDTO = authenticationService.refresh(refreshTokenRequestDTO);

        return new ApiResponse<>(true, "Token atualizado com sucesso", loginResponseDTO);
    }
}

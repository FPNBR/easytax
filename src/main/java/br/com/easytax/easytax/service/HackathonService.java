package br.com.easytax.easytax.service;

import br.com.easytax.easytax.client.HackathonClient;
import br.com.easytax.easytax.dto.response.HackathonResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HackathonService {
    private final HackathonClient hackathonClient;

    public List<HackathonResponseDTO> getHackathonData(String cpf) {
        String apiKey = "HACKATON_UNIESP_MARJO_2024";
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var currentUser = jwt.getSubject();
        List<String> roles = jwt.getClaim("roles");

        if (roles.contains("ADMIN") || currentUser.equals(cpf)) {
            return hackathonClient.getHackathonData(cpf, apiKey);
        } else {
            throw new AccessDeniedException("Você não tem permissão para acessar informações de outro usuário");
        }
    }
}
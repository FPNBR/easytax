package br.com.easytax.easytax.service;

import br.com.easytax.easytax.client.MarjosportsClient;
import br.com.easytax.easytax.dto.response.TransactionsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionsService {
    private final MarjosportsClient marjosportsClient;

    public List<TransactionsResponseDTO> getTransactions(String cpf) {
        String apiKey = "HACKATON_UNIESP_MARJO_2024";
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var currentUser = jwt.getSubject();
        List<String> roles = jwt.getClaim("roles");

        if (roles.contains("ADMIN") || currentUser.equals(cpf)) {
            return marjosportsClient.getTransactions(cpf, apiKey);
        } else {
            throw new AccessDeniedException("Você não tem permissão para acessar informações de outro usuário");
        }
    }
}
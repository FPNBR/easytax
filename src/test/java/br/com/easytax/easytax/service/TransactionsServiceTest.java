package br.com.easytax.easytax.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.easytax.easytax.client.MarjosportsClient;
import br.com.easytax.easytax.dto.response.TransactionsResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionsServiceTest {

    @Mock
    private MarjosportsClient marjosportsClient;

    @InjectMocks
    private TransactionsService transactionsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private void mockSecurityContext(String subject, List<String> roles) {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", subject)
                .claim("roles", roles)
                .build();

        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, authorities);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetTransactionsWithAdminRole() {
        // Mock data
        String cpf = "12345678901";
        String apiKey = "HACKATON_UNIESP_MARJO_2024";

        mockSecurityContext("admin", List.of("ADMIN"));

        List<TransactionsResponseDTO> mockResponse = List.of(new TransactionsResponseDTO());
        when(marjosportsClient.getTransactions(cpf, apiKey)).thenReturn(mockResponse);

        // Method execution
        List<TransactionsResponseDTO> response = transactionsService.getTransactions(cpf);

        // Verification
        assertEquals(mockResponse, response);
        verify(marjosportsClient, times(1)).getTransactions(cpf, apiKey);
    }

    @Test
    public void testGetTransactionsWithUserRole() {
        // Mock data
        String cpf = "12345678901";
        String apiKey = "HACKATON_UNIESP_MARJO_2024";

        mockSecurityContext(cpf, List.of("USER"));

        List<TransactionsResponseDTO> mockResponse = List.of(new TransactionsResponseDTO());
        when(marjosportsClient.getTransactions(cpf, apiKey)).thenReturn(mockResponse);

        // Method execution
        List<TransactionsResponseDTO> response = transactionsService.getTransactions(cpf);

        // Verification
        assertEquals(mockResponse, response);
        verify(marjosportsClient, times(1)).getTransactions(cpf, apiKey);
    }

    @Test
    public void testGetTransactionsAccessDenied() {
        // Mock data
        String cpf = "12345678901";
        String apiKey = "HACKATON_UNIESP_MARJO_2024";

        mockSecurityContext("anotherUser", List.of("USER"));

        // Method execution and verification
        assertThrows(AccessDeniedException.class, () -> transactionsService.getTransactions(cpf));
    }
}
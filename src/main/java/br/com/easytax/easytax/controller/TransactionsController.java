package br.com.easytax.easytax.controller;

import br.com.easytax.easytax.dto.response.TransactionsResponseDTO;
import br.com.easytax.easytax.service.TransactionsService;
import br.com.easytax.easytax.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Transações Fiscais", description = "Endpoint responsável em retornar as transações fiscais do usuário proveniente de doações")
@RequestMapping("/transactions")
public class TransactionsController {
    private final TransactionsService transactionsService;

    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_CLIENT"})
    @GetMapping
    public ApiResponse<List<TransactionsResponseDTO>> getTransactions(@RequestParam("cpf") String cpf) {
        List<TransactionsResponseDTO> data = transactionsService.getTransactions(cpf);

        return new ApiResponse<>(true, "Transações recuperadas com sucesso", data);
    }
}
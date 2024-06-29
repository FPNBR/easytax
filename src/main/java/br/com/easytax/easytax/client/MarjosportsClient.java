package br.com.easytax.easytax.client;

import br.com.easytax.easytax.dto.response.TransactionsResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "marjosportsClient", url = "https://hackathon.marjosports.com.br")
public interface MarjosportsClient {

    @GetMapping("/hackathon")
    List<TransactionsResponseDTO> getTransactions(@RequestParam("cpf") String cpf, @RequestHeader("api-key") String apiKey);
}

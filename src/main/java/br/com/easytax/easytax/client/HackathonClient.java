package br.com.easytax.easytax.client;

import br.com.easytax.easytax.dto.request.HackathonRequestDTO;
import br.com.easytax.easytax.dto.response.HackathonResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "hackathonClient", url = "https://hackathon.marjosports.com.br")
public interface HackathonClient {

    @GetMapping("/hackathon")
    List<HackathonResponseDTO> getHackathonData(@RequestParam("cpf") String cpf, @RequestHeader("api-key") String apiKey);

    @PostMapping("/hackathon")
    HackathonResponseDTO postHackathonData(@RequestBody HackathonRequestDTO requestDTO, @RequestHeader("api-key") String apiKey);
}

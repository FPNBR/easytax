package br.com.easytax.easytax.controller;

import br.com.easytax.easytax.dto.response.HackathonResponseDTO;
import br.com.easytax.easytax.service.HackathonService;
import br.com.easytax.easytax.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/hackathon")
public class HackathonController {
    private final HackathonService hackathonService;

    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_CLIENT"})
    @GetMapping
    public ApiResponse<List<HackathonResponseDTO>> getHackathonData(@RequestParam("cpf") String cpf) {
        List<HackathonResponseDTO> data = hackathonService.getHackathonData(cpf);

        return new ApiResponse<>(true, "Dados do Hackathon recuperados com sucesso", data);
    }
}
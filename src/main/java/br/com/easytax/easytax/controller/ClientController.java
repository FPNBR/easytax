package br.com.easytax.easytax.controller;

import br.com.easytax.easytax.repository.ClientRepository;
import br.com.easytax.easytax.dto.request.ClientRequestDTO;
import br.com.easytax.easytax.dto.response.ClientResponseDTO;
import br.com.easytax.easytax.service.ClientService;
import br.com.easytax.easytax.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Clientes", description = "Endpoint responsável por realizar operações com os clientes")
@RequestMapping("/client")
public class ClientController {

    private final ModelMapper modelMapper;
    private final ClientService clientService;
    private final ClientRepository clientRepository;

    @ResponseStatus(HttpStatus.OK)
    @Secured("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<ClientResponseDTO>> getAllClients() {
        List<ClientResponseDTO> Clients = clientService.getAllClients();

        return new ApiResponse<>(true, "Usuários encontrados", Clients);
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse<ClientResponseDTO> getClientById(@PathVariable Long id) {
        ClientResponseDTO Client = clientService.getClientById(id);

        return new ApiResponse<>(true, "Usuário encontrado", Client);
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured("hasRole('ADMIN')")
    @GetMapping("/cpf")
    public ApiResponse<ClientResponseDTO> getClientByCpf(@RequestParam String cpf) {
        ClientResponseDTO Client = clientService.getClientByCpf(cpf);

        return new ApiResponse<>(true, "Usuário encontrado", Client);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<ClientResponseDTO> createClient(@RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO createdClient = clientService.createClient(clientRequestDTO);

        return new ApiResponse<>(true, "Usuário criado com sucesso", createdClient);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/password")
    public ApiResponse<Void> updatePassword(@PathVariable Long id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        clientService.updatePassword(id, oldPassword, newPassword);

        return new ApiResponse<>(true, "Senha atualizada com sucesso", null);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);

        return new ApiResponse<>(true, "Usuário deletado com sucesso", null);
    }
}

package br.com.easytax.easytax.controller;

import br.com.easytax.easytax.repository.ClientRepository;
import br.com.easytax.easytax.dto.request.ClientRequestDTO;
import br.com.easytax.easytax.dto.response.ClientResponseDTO;
import br.com.easytax.easytax.service.ClientService;
import br.com.easytax.easytax.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/client")
public class ClientController {

    private final ModelMapper modelMapper;
    private final ClientService clientService;
    private final ClientRepository clientRepository;

    @ResponseStatus(HttpStatus.OK)
    @Secured("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<ClientResponseDTO>> getAllUsers() {
        List<ClientResponseDTO> users = clientService.getAllUsers();

        return new ApiResponse<>(true, "Usuários encontrados", users);
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse<ClientResponseDTO> getUserById(@PathVariable Long id) {
        ClientResponseDTO user = clientService.getUserById(id);

        return new ApiResponse<>(true, "Usuário encontrado", user);
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured("hasRole('ADMIN')")
    @GetMapping("/cpf")
    public ApiResponse<ClientResponseDTO> getUserByCpf(@RequestParam String cpf) {
        ClientResponseDTO user = clientService.getUserByCpf(cpf);

        return new ApiResponse<>(true, "Usuário encontrado", user);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<ClientResponseDTO> createUser(@RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO createdUser = clientService.createUser(clientRequestDTO);

        return new ApiResponse<>(true, "Usuário criado com sucesso", createdUser);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}/password")
    public ApiResponse<Void> updatePassword(@PathVariable Long id, @RequestParam String oldPassword, @RequestParam String newPassword) {
        clientService.updatePassword(id, oldPassword, newPassword);

        return new ApiResponse<>(true, "Senha atualizada com sucesso", null);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        clientService.deleteUser(id);

        return new ApiResponse<>(true, "Usuário deletado com sucesso", null);
    }
}

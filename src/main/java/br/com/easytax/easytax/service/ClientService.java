package br.com.easytax.easytax.service;

import br.com.easytax.easytax.domain.Client;
import br.com.easytax.easytax.domain.Role;
import br.com.easytax.easytax.exception.InvalidCpfException;
import br.com.easytax.easytax.exception.ResourceNotFoundException;
import br.com.easytax.easytax.exception.UniqueConstraintViolationException;
import br.com.easytax.easytax.repository.*;
import br.com.easytax.easytax.dto.request.ClientRequestDTO;
import br.com.easytax.easytax.dto.response.ClientResponseDTO;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<ClientResponseDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(Client -> modelMapper.map(Client, ClientResponseDTO.class))
                .collect(Collectors.toList());
    }

    public ClientResponseDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        return modelMapper.map(client, ClientResponseDTO.class);
    }

    public ClientResponseDTO getClientByCpf(String cpf) {
        Client client = clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        return modelMapper.map(client, ClientResponseDTO.class);
    }

    public ClientResponseDTO createClient(ClientRequestDTO clientRequestDTO) {
        Client client = modelMapper.map(clientRequestDTO, Client.class);
        client.setPassword(bCryptPasswordEncoder.encode(client.getPassword()));

        Role ClientRole = roleRepository.findByName("CLIENTE")
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não criado, Role 'CLIENTE' não encontrada no banco"));
        client.setRoles(Collections.singletonList(ClientRole));

        try {
            Client savedClient = clientRepository.save(client);
            return modelMapper.map(savedClient, ClientResponseDTO.class);
        } catch (DataIntegrityViolationException ex) {
            throw new UniqueConstraintViolationException("O Cliente já está cadastrado no sistema");
        } catch (ConstraintViolationException ex) {
            throw new InvalidCpfException("CPF inválido");
        }
    }

    public void updatePassword(Long id, String oldPassword, String newPassword) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        checkClientPermission(client.getCpf());

        if (!bCryptPasswordEncoder.matches(oldPassword, client.getPassword())) {
            throw new IllegalArgumentException("A senha antiga não corresponde à senha atual do Cliente");
        }

        client.setPassword(bCryptPasswordEncoder.encode(newPassword));

        clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        checkClientPermission(client.getCpf());

        clientRepository.delete(client);
    }

    private static void checkClientPermission(String Client) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var currentClient = jwt.getSubject();

        if (!currentClient.equals(Client)) {
            throw new AccessDeniedException("Você não tem permissão para atualizar informações de outro Cliente");
        }
    }
}

package br.com.easytax.easytax.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.easytax.easytax.domain.Client;
import br.com.easytax.easytax.domain.Role;
import br.com.easytax.easytax.dto.request.ClientRequestDTO;
import br.com.easytax.easytax.dto.response.ClientResponseDTO;
import br.com.easytax.easytax.exception.InvalidCpfException;
import br.com.easytax.easytax.exception.ResourceNotFoundException;
import br.com.easytax.easytax.exception.UniqueConstraintViolationException;
import br.com.easytax.easytax.repository.ClientRepository;
import br.com.easytax.easytax.repository.RoleRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateClientSuccess() {
        // Mock data
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setCpf("12345678901");
        clientRequestDTO.setPassword("password123");

        Client client = new Client();
        client.setCpf("12345678901");
        client.setPassword("password123");

        Role clientRole = new Role();
        clientRole.setName("CLIENTE");

        Client savedClient = new Client();
        savedClient.setCpf("12345678901");
        savedClient.setPassword("encodedPassword");

        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setCpf("12345678901");

        // Mocking the behavior
        when(modelMapper.map(clientRequestDTO, Client.class)).thenReturn(client);
        when(bCryptPasswordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.of(clientRole));
        when(clientRepository.save(client)).thenReturn(savedClient);
        when(modelMapper.map(savedClient, ClientResponseDTO.class)).thenReturn(clientResponseDTO);

        // Method execution
        ClientResponseDTO response = clientService.createClient(clientRequestDTO);

        // Verification
        assertEquals("12345678901", response.getCpf());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void testCreateClientRoleNotFound() {
        // Mock data
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setCpf("12345678901");
        clientRequestDTO.setPassword("password123");

        Client client = new Client();
        client.setCpf("12345678901");
        client.setPassword("password123");

        // Mocking the behavior
        when(modelMapper.map(clientRequestDTO, Client.class)).thenReturn(client);
        when(bCryptPasswordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.empty());

        // Method execution and verification
        assertThrows(ResourceNotFoundException.class, () -> clientService.createClient(clientRequestDTO));
    }

    @Test
    public void testCreateClientUniqueConstraintViolation() {
        // Mock data
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setCpf("12345678901");
        clientRequestDTO.setPassword("password123");

        Client client = new Client();
        client.setCpf("12345678901");
        client.setPassword("password123");

        Role clientRole = new Role();
        clientRole.setName("CLIENTE");

        // Mocking the behavior
        when(modelMapper.map(clientRequestDTO, Client.class)).thenReturn(client);
        when(bCryptPasswordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.of(clientRole));
        when(clientRepository.save(client)).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        // Method execution and verification
        assertThrows(UniqueConstraintViolationException.class, () -> clientService.createClient(clientRequestDTO));
    }

    @Test
    public void testCreateClientInvalidCpf() {
        // Mock data
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setCpf("123");
        clientRequestDTO.setPassword("password123");

        Client client = new Client();
        client.setCpf("123");
        client.setPassword("password123");

        Role clientRole = new Role();
        clientRole.setName("CLIENTE");

        // Mocking the behavior
        when(modelMapper.map(clientRequestDTO, Client.class)).thenReturn(client);
        when(bCryptPasswordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findByName("CLIENTE")).thenReturn(Optional.of(clientRole));
        when(clientRepository.save(client)).thenThrow(new ConstraintViolationException(null, null));

        // Method execution and verification
        assertThrows(InvalidCpfException.class, () -> clientService.createClient(clientRequestDTO));
    }
}
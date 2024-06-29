package br.com.easytax.easytax.service;

import br.com.easytax.easytax.domain.Role;
import br.com.easytax.easytax.dto.RoleDTO;
import br.com.easytax.easytax.exception.ResourceNotFoundException;
import br.com.easytax.easytax.exception.UniqueConstraintViolationException;
import br.com.easytax.easytax.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Regra não encontrada"));
        return modelMapper.map(role, RoleDTO.class);
    }

    public RoleDTO createRole(RoleDTO roleDto) {
        Role role = modelMapper.map(roleDto, Role.class);
        try {
            Role savedRole = roleRepository.save(role);
            return modelMapper.map(savedRole, RoleDTO.class);
        } catch (DataIntegrityViolationException ex) {
            throw new UniqueConstraintViolationException(String.format("Regra com o nome '%s' já existe", role.getName()));
        }
    }

    public RoleDTO updateRole(Long id, RoleDTO roleDto) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Regra não encontrada"));
        existingRole.setName(roleDto.getName());
        Role updatedRole = roleRepository.save(existingRole);
        return modelMapper.map(updatedRole, RoleDTO.class);
    }

    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Regra não encontrada"));
        roleRepository.delete(role);
    }
}

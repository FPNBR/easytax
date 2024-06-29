package br.com.easytax.easytax.controller;

import br.com.easytax.easytax.dto.RoleDTO;
import br.com.easytax.easytax.service.RoleService;
import br.com.easytax.easytax.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Papéis do Usuário", description = "Endpoint para gerenciar os níveis de acesso no sistema")
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    @GetMapping
    public ApiResponse<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();

        return new ApiResponse<>(true, "Regras encontradas", roles);
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    public ApiResponse<RoleDTO> getRoleById(@PathVariable Long id) {
        RoleDTO role = roleService.getRoleById(id);

        return new ApiResponse<>(true, "Regra encontrada", role);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_ADMIN")
    @PostMapping
    public ApiResponse<RoleDTO> createRole(@RequestBody RoleDTO roleDto) {
        RoleDTO createdRole = roleService.createRole(roleDto);

        return new ApiResponse<>(true, "Regra criada com sucesso", createdRole);
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ApiResponse<RoleDTO> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDto) {
        RoleDTO updatedRole = roleService.updateRole(id, roleDto);

        return new ApiResponse<>(true,"Regra atualizada com sucesso", updatedRole);
    }

    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);

        return new ApiResponse<>(true, "Regra deletada com sucesso", null);
    }
}

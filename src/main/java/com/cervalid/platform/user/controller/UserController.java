package com.cervalid.platform.user.controller;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.user.dto.filter.UserFilterRequest;
import com.cervalid.platform.user.dto.request.CreateUserRequest;
import com.cervalid.platform.user.dto.request.LinkInstitutionRequest;
import com.cervalid.platform.user.dto.request.UpdateUserRequest;
import com.cervalid.platform.user.dto.request.UpdateUserRoleRequest;
import com.cervalid.platform.user.dto.response.UserResponse;
import com.cervalid.platform.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Listar usuarios según el contexto y los filtros
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping
    public Page<UserResponse> listUsers(
            @RequestParam(required = false) Long institutionId,
            @RequestParam(required = false) RoleName role,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {

        UserFilterRequest filter = new UserFilterRequest();
        filter.setInstitutionId(institutionId);
        filter.setRole(role);
        filter.setActive(active);

        return userService.listUsers(filter, pageable);
    }

    // Listar usuarios para el superadministrador
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/admin")
    public Page<UserResponse> listAllUsers(
            @RequestParam(required = false) Long institutionId,
            @RequestParam(required = false) RoleName role,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {

        UserFilterRequest filter = new UserFilterRequest();
        filter.setInstitutionId(institutionId);
        filter.setRole(role);
        filter.setActive(active);

        return userService.listAllUsers(filter, pageable);
    }

    // Listar usuarios de la institución activa
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping("/institution")
    public Page<UserResponse> listInstitutionUsers(
            @RequestParam(required = false) RoleName role,
            @RequestParam(required = false) Boolean active,
            Pageable pageable) {

        UserFilterRequest filter = new UserFilterRequest();
        filter.setRole(role);
        filter.setActive(active);

        return userService.listUsersByInstitution(filter, pageable);
    }

    // Consultar un usuario específico
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping("/{id}")
    public UserResponse getUser(
            @PathVariable Long id) {
        return userService.getUser(id);
    }

    // Crear usuario
    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping
    public UserResponse createUser(
            @RequestBody CreateUserRequest request) {

        return userService.createUser(request);
    }

    // Eliminar usuario o su pertenencia institucional
    @PreAuthorize("hasAuthority('DELETE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    // Actualizar el rol de un usuario
    @PreAuthorize("hasAuthority('EDIT_USER')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable Long id,
            @RequestBody UpdateUserRoleRequest request) {

        userService.updateUserRole(id, request);

        return ResponseEntity.ok("Rol actualizado correctamente");
    }

    // Actualizar datos comunes del usuario
    @PreAuthorize("hasAuthority('EDIT_USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {

        UserResponse updated = userService.updateUser(id, request);

        return ResponseEntity.ok(updated);
    }

    // Vincular un usuario existente a otra institución
    @PostMapping("/link-institution")
    public ResponseEntity<String> linkInstitution(
            @RequestBody LinkInstitutionRequest request) {

        userService.linkInstitution(request);

        return ResponseEntity.ok("Usuario vinculado a la institución");
    }
}

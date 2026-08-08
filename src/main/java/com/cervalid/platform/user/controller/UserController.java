package com.cervalid.platform.user.controller;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.user.dto.*;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // para listar usuarios
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

    // listar usuarios para el frontend superadmin
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

    // listar los usuarios de una institución en especifica
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

    // ver un usuario específico
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    // crear usuario
    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping
    public User createUser(
            @RequestBody CreateUserRequest request) {

        return userService.createUser(request);
    }

    @PreAuthorize("hasAuthority('DELETE_USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // endpoint PATCH - actualizar rol - EDIT_USER
    @PreAuthorize("hasAuthority('EDIT_USER')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long id,
            @RequestBody UpdateUserRoleRequest request) {

        userService.updateUserRole(id, request);
        return ResponseEntity.ok("Rol actualizado correctamente");
    }

    // endpoint PATCH - actualizar usuario
    @PreAuthorize("hasAuthority('EDIT_USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {

        User updated = userService.updateUser(id, request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/link-institution")
    public ResponseEntity<?> linkInstitution(
            @RequestBody LinkInstitutionRequest request) {

        userService.linkInstitution(request);

        return ResponseEntity.ok("Usuario vinculado a la institución");
    }

}

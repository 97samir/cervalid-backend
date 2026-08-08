package com.cervalid.platform.user.controller;

import com.cervalid.platform.user.dto.ChangeStatusRequest;
import com.cervalid.platform.user.service.InstitutionUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/institution-users")
@RequiredArgsConstructor
public class InstitutionUserController {

    private final InstitutionUserService institutionUserService;

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(
            @PathVariable Long id,
            @RequestBody ChangeStatusRequest request) {

        institutionUserService.changeStatus(id, request.isActive());

        return ResponseEntity.ok("Estado actualizado");
    }
}

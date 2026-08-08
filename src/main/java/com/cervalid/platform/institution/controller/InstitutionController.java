package com.cervalid.platform.institution.controller;

import com.cervalid.platform.institution.dto.*;
import com.cervalid.platform.institution.service.InstitutionService;
import com.cervalid.platform.institution.entity.Institution;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/institutions")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @GetMapping
    public List<InstitutionResponse> listResponse(
            @RequestParam(required = false) Boolean active) {
        return institutionService.listInstitutions(active);
    }

    // para datos propios de institución
    @GetMapping("/me")
    public ResponseEntity<InstitutionResponse> getMyInstitution() {
        return ResponseEntity.ok(institutionService.getMyInstitution());
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<InstitutionResponse> create(
            @RequestBody CreateInstitutionRequest request) {

        InstitutionResponse response = institutionService.createInstitution(request);
        return ResponseEntity.ok(response);
    }

    // editar - reciclado de /users)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateInstitution(
            @PathVariable Long id,
            @RequestBody UpdateInstitutionRequest request) {

        institutionService.updateInstitution(id, request);

        return ResponseEntity.ok("Institución actualizada");
    }

    // muestra resumen de la institucion
    @GetMapping("/dashboard-stats")
    public DashboardStatsResponse getDashboardStats(){
        return institutionService.getDashboardStats();
    }

    // actualizar estado de institucion
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> changeStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {

        institutionService.changeStatus(id, active);
        return ResponseEntity.ok("Estado actualizado");
    }

}

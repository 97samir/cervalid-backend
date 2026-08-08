package com.cervalid.platform.tenant.controller;

import com.cervalid.platform.tenant.dto.InstitutionRequestDTO;
import com.cervalid.platform.tenant.dto.InstitutionRequestResponse;
import com.cervalid.platform.tenant.entity.InstitutionRequest;
import com.cervalid.platform.tenant.service.InstitutionRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/institution-requests")
@RequiredArgsConstructor
public class InstitutionRequestController {

    private final InstitutionRequestService requestService;

    @PostMapping
    public ResponseEntity<InstitutionRequestResponse> createRequest(
            @Valid @RequestBody InstitutionRequestDTO request) {

        InstitutionRequestResponse saved = requestService.registerRequest(request);
        return ResponseEntity.ok(saved);
    }

    // obtener lista de instituciones
    @GetMapping
    public ResponseEntity<List<InstitutionRequestResponse>> getRequests(
            @RequestParam(required = false) String status) {

        if (status == null) {
            return ResponseEntity.ok(requestService.getAllRequests());
        }

        return ResponseEntity.ok(requestService.getRequestsByStatus(status));
    }
}

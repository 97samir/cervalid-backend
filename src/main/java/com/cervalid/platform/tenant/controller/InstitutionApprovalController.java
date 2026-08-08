package com.cervalid.platform.tenant.controller;

import com.cervalid.platform.tenant.dto.ApproveInstitutionRequest;
import com.cervalid.platform.tenant.dto.ApproveInstitutionResponse;
import com.cervalid.platform.tenant.dto.RejectInstitutionRequest;
import com.cervalid.platform.tenant.service.InstitutionApprovalService;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/institution-requests")

public class InstitutionApprovalController {

    private final InstitutionApprovalService approvalService;
    private final UserRepository userRepository;

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApproveInstitutionResponse> approve(
            @PathVariable Long id,
            @RequestBody ApproveInstitutionRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User superAdmin = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ApproveInstitutionResponse response =
                approvalService.approveRequest(
                        id,
                        superAdmin,
                        request.getWalletAddress()
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> reject(
            @PathVariable Long id,
            @RequestBody RejectInstitutionRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User superAdmin = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        approvalService.rejectRequest(id, request.getReason(), superAdmin);

        return ResponseEntity.ok("Solicitud rechazada");
    }
}

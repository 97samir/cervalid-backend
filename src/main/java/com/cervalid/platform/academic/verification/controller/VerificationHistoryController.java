package com.cervalid.platform.academic.verification.controller;

import com.cervalid.platform.academic.verification.dto.request.VerificationHistoryFilterRequest;
import com.cervalid.platform.academic.verification.dto.response.VerificationHistoryResponse;
import com.cervalid.platform.academic.verification.service.VerificationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/verification/history")
@RequiredArgsConstructor
public class VerificationHistoryController {

    private final VerificationHistoryService verificationHistoryService;

    @GetMapping("/certificate/{certificatePublicId}")
    public Page<VerificationHistoryResponse> byCertificate(
            @PathVariable UUID certificatePublicId,
            Pageable pageable) {

        return verificationHistoryService.getByCertificate(
                certificatePublicId,
                pageable);
    }

    @GetMapping("/institution")
    public Page<VerificationHistoryResponse> byInstitution(
            Pageable pageable) {

        return verificationHistoryService.getByInstitution(
                pageable);
    }

    @PostMapping("/search")
    public Page<VerificationHistoryResponse> search(
            @RequestBody VerificationHistoryFilterRequest request,
            Pageable pageable) {

        return verificationHistoryService.search(
                request,
                pageable);
    }
}
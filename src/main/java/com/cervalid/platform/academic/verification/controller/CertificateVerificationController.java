package com.cervalid.platform.academic.verification.controller;

import com.cervalid.platform.academic.verification.dto.request.VerifyCertificateRequest;
import com.cervalid.platform.academic.verification.dto.response.VerifyCertificateResponse;
import com.cervalid.platform.academic.verification.service.CertificateVerificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/verification/certificates")
@RequiredArgsConstructor
public class CertificateVerificationController {

    private final CertificateVerificationService verificationService;

    @PostMapping("/verify")
    public VerifyCertificateResponse verify(
            @RequestBody VerifyCertificateRequest request,
            HttpServletRequest httpRequest) {

        String ipAddress = httpRequest.getRemoteAddr();

        return verificationService.verify(request, ipAddress);
    }

    @GetMapping("/{certificatePublicId}")
    public VerifyCertificateResponse verifyPublic(
            @PathVariable UUID certificatePublicId,
            HttpServletRequest request
    ){

        String ip =
                request.getRemoteAddr();

        return verificationService.verifyPublic(
                certificatePublicId,
                ip
        );
    }
}

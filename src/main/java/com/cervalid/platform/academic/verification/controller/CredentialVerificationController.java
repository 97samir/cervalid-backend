package com.cervalid.platform.academic.verification.controller;

import com.cervalid.platform.academic.credential.dto.response.CredentialVerificationResponse;
import com.cervalid.platform.academic.credential.service.CredentialService;
import com.cervalid.platform.academic.timeline.dto.view.PublicTimelineEventView;
import com.cervalid.platform.academic.verification.dto.request.VerifyCredentialRequest;
import com.cervalid.platform.academic.verification.dto.response.CredentialVerificationResultResponse;
import com.cervalid.platform.academic.verification.service.CredentialVerificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/verification/credentials")
@RequiredArgsConstructor
public class CredentialVerificationController {

    private final CredentialVerificationService verificationService;
    private final CredentialService credentialService;

    @GetMapping("/{credentialPublicId}")
    public CredentialVerificationResponse verifyPublic(
            @PathVariable UUID credentialPublicId
    ) {

        return credentialService.getVerificationByPublicId(
                credentialPublicId
        );
    }

    @PostMapping("/verify")
    public CredentialVerificationResultResponse verify(
            @Valid @RequestBody VerifyCredentialRequest request,
            HttpServletRequest httpRequest
    ) {

        String ipAddress =
                httpRequest.getRemoteAddr();

        return verificationService.verify(
                request,
                ipAddress
        );
    }

    @GetMapping("/{credentialPublicId}/timeline")
    public List<PublicTimelineEventView> getPublicTimeline(
            @PathVariable UUID credentialPublicId
    ) {

        return verificationService.getPublicTimeline(
                credentialPublicId
        );
    }
}
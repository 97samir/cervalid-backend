package com.cervalid.platform.academic.certificate.controller;

import com.cervalid.platform.academic.certificate.dto.request.CertificateSearchRequest;
import com.cervalid.platform.academic.certificate.dto.request.IssueCertificateRequest;
import com.cervalid.platform.academic.certificate.dto.response.CertificateDetailResponse;
import com.cervalid.platform.academic.certificate.dto.response.CertificateResponse;
import com.cervalid.platform.academic.certificate.service.CertificateService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/academic/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    // CREA / EMITE - ISSUE FROM TRANSCRIPT (RESTFUL)
    @PostMapping("/transcripts/{transcriptPublicId}")
    public ResponseEntity<CertificateResponse> issueFromTranscript(
            @PathVariable UUID transcriptPublicId) {

        return ResponseEntity.ok(certificateService
                .issueFromTranscript(transcriptPublicId)
        );
    }

    // CREA - INTERNAL ORCHESTRATION ENDPOINT
    @PostMapping
    public ResponseEntity<CertificateResponse> issue(
            @RequestBody IssueCertificateRequest request) {
        return ResponseEntity.ok(certificateService.issue(request));
    }

    // certificado por estudiante
    @GetMapping("/students/{studentPublicId}")
    public ResponseEntity<Page<CertificateResponse>> getByStudent(
            @PathVariable UUID studentPublicId,
            Pageable pageable) {

        return ResponseEntity.ok(
                certificateService.getByStudent(
                        studentPublicId,
                        pageable
                )
        );
    }

    @GetMapping
    public ResponseEntity<Page<CertificateResponse>> search(
            CertificateSearchRequest request,
            Pageable pageable) {

        return ResponseEntity.ok(
                certificateService.search(
                        request,
                        pageable
                )
        );
    }

    // GET BY PUBLIC ID
    @GetMapping("/{publicId}")
    public ResponseEntity<CertificateResponse> getByPublicId(
            @PathVariable UUID publicId) {

        return ResponseEntity.ok(
                certificateService.getByPublicId(publicId)
        );
    }

    // GET BY CERTIFICATE NUMBER
    @GetMapping("/number/{certificateNumber}")
    public ResponseEntity<CertificateResponse> getByCertificateNumber(
            @PathVariable String certificateNumber) {
        return ResponseEntity.ok(
                certificateService.getByCertificateNumber(
                        certificateNumber
                )
        );
    }

    // REVOKE CERTIFICATE
    @PatchMapping("/{publicId}/revoke")
    public ResponseEntity<Void> revoke(
            @PathVariable UUID publicId) {
        certificateService.revoke(publicId);
        return ResponseEntity.noContent().build();
    }

    //detail response
    @GetMapping("/{publicId}/detail")
    public ResponseEntity<CertificateDetailResponse> detail(
            @PathVariable UUID publicId) {

        System.out.println("PUBLIC ID RECIBIDO = " + publicId);

        return ResponseEntity.ok(
                certificateService.getDetail(publicId)
        );
    }
}
package com.cervalid.platform.academic.credential.controller;

import com.cervalid.platform.academic.credential.dto.filter.CredentialFilterRequest;
import com.cervalid.platform.academic.credential.dto.request.CreateCredentialRequest;
import com.cervalid.platform.academic.credential.dto.request.RevokeCredentialRequest;
import com.cervalid.platform.academic.credential.dto.request.UpdateCredentialRequest;
import com.cervalid.platform.academic.credential.dto.response.CredentialDetailResponse;
import com.cervalid.platform.academic.credential.dto.response.CredentialDocumentResponse;
import com.cervalid.platform.academic.credential.dto.response.CredentialResponse;
import com.cervalid.platform.academic.credential.service.CredentialService;
import com.cervalid.platform.security.context.SecurityContextService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/academic/credentials")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService credentialService;
    private final SecurityContextService securityContextService;

    @PostMapping("/students/{studentPublicId}")
    public ResponseEntity<CredentialResponse> create(
            @PathVariable UUID studentPublicId,
            @Valid @RequestBody CreateCredentialRequest request) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        credentialService.create(
                                institutionId,
                                studentPublicId,
                                request
                        )
                );
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<CredentialDetailResponse> getByPublicId(
            @PathVariable UUID publicId) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return ResponseEntity.ok(
                credentialService.getByPublicId(
                        institutionId,
                        publicId
                )
        );
    }

    @GetMapping("/students/{studentPublicId}")
    public ResponseEntity<Page<CredentialResponse>> getByStudent(
            @PathVariable UUID studentPublicId,
            Pageable pageable) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return ResponseEntity.ok(
                credentialService.getByStudent(
                        institutionId,
                        studentPublicId,
                        pageable
                )
        );
    }

    @GetMapping
    public ResponseEntity<Page<CredentialResponse>> search(
            CredentialFilterRequest filter,
            Pageable pageable) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return ResponseEntity.ok(
                credentialService.search(
                        institutionId,
                        filter,
                        pageable
                )
        );
    }

    @PutMapping("/{publicId}")
    public ResponseEntity<CredentialResponse> update(
            @PathVariable UUID publicId,
            @Valid @RequestBody UpdateCredentialRequest request) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return ResponseEntity.ok(
                credentialService.update(
                        institutionId,
                        publicId,
                        request
                )
        );
    }

    @PatchMapping("/{publicId}/issue")
    public ResponseEntity<CredentialResponse> issue(
            @PathVariable UUID publicId) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return ResponseEntity.ok(credentialService
                .issue(
                        institutionId,
                        publicId)
        );
    }

    @PatchMapping("/{publicId}/revoke")
    public ResponseEntity<CredentialResponse> revoke(
            @PathVariable UUID publicId,
            @Valid @RequestBody RevokeCredentialRequest request) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return ResponseEntity.ok(
                credentialService.revoke(
                        institutionId,
                        publicId,
                        request
                )
        );
    }

    @PutMapping(value = "/{publicId}/document",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CredentialDocumentResponse> updateDocument(

            @PathVariable UUID publicId,
            @RequestPart(value = "file", required = false)
            MultipartFile file,
            @RequestParam(value = "documentHash", required = false)
            String documentHash,
            @RequestParam(value = "documentUrl", required = false)
            String documentUrl
    ) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return ResponseEntity.ok(
                credentialService.updateDocument(
                        institutionId,
                        publicId,
                        file,
                        documentHash,
                        documentUrl
                )
        );
    }
}
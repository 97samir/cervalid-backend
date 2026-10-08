package com.cervalid.platform.academic.verification.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.certificate.service.CertificateQueryService;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.repository.CredentialRepository;
import com.cervalid.platform.academic.verification.dto.request.VerificationHistoryFilterRequest;
import com.cervalid.platform.academic.verification.dto.response.VerificationHistoryResponse;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import com.cervalid.platform.academic.verification.mapper.VerificationHistoryMapper;
import com.cervalid.platform.academic.verification.repository.VerificationRecordRepository;
import com.cervalid.platform.academic.verification.specification.VerificationSpecification;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationHistoryService {

    private final VerificationRecordRepository repository;
    private final CertificateRepository certificateRepository;
    private final CredentialRepository credentialRepository;
    private final SecurityContextService securityContextService;
    private final VerificationHistoryMapper verificationHistoryMapper;
    private final VerificationQueryService verificationQueryService;

    public Page<VerificationHistoryResponse> getByCertificate(
            UUID certificatePublicId,
            Pageable pageable) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Certificate certificate =
                certificateRepository
                        .findByPublicIdAndInstitutionId(
                                certificatePublicId,
                                institutionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Certificate not found"));

        return repository
                .findByCertificateIdAndInstitutionIdOrderByVerifiedAtDesc(
                        certificate.getId(),
                        institutionId,
                        pageable)
                .map(verificationQueryService::buildHistoryView)
                .map(verificationHistoryMapper::toResponse);
    }

    public Page<VerificationHistoryResponse> getByCredential(
            UUID credentialPublicId,
            Pageable pageable) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Credential credential =
                credentialRepository
                        .findByPublicIdAndInstitutionIdAndDeletedFalse(
                                credentialPublicId,
                                institutionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Credential not found")
                        );

        return repository
                .findByCredentialIdAndInstitutionIdOrderByVerifiedAtDesc(
                        credential.getId(),
                        institutionId,
                        pageable
                )
                .map(verificationQueryService::buildHistoryView)
                .map(verificationHistoryMapper::toResponse);
    }

    public Page<VerificationHistoryResponse> getByInstitution(
            Pageable pageable) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return repository
                .findByInstitutionIdOrderByVerifiedAtDesc(
                        institutionId,
                        pageable)
                .map(verificationQueryService::buildHistoryView)
                .map(verificationHistoryMapper::toResponse);
    }

    public Page<VerificationHistoryResponse> search(
            VerificationHistoryFilterRequest request,
            Pageable pageable) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return repository
                .findAll(
                        VerificationSpecification.filter(
                                request,
                                institutionId
                        ),
                        pageable
                )
                .map(verificationQueryService::buildHistoryView)
                .map(verificationHistoryMapper::toResponse);
    }
}
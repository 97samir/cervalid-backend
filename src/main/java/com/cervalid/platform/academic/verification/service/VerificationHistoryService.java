package com.cervalid.platform.academic.verification.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.certificate.service.CertificateQueryService;
import com.cervalid.platform.academic.verification.dto.request.VerificationHistoryFilterRequest;
import com.cervalid.platform.academic.verification.dto.response.VerificationHistoryResponse;
import com.cervalid.platform.academic.verification.dto.view.VerificationView;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import com.cervalid.platform.academic.verification.mapper.VerificationHistoryMapper;
import com.cervalid.platform.academic.verification.repository.VerificationRecordRepository;
import com.cervalid.platform.academic.verification.specification.VerificationSpecification;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationHistoryService {

    private final VerificationRecordRepository repository;
    private final CertificateRepository certificateRepository;
    private final SecurityContextService securityContextService;
    private final VerificationHistoryMapper verificationHistoryMapper;
    private final CertificateQueryService certificateQueryService;
    private final VerificationQueryService verificationQueryService;

    public List<VerificationHistoryResponse> getByCertificate(
            UUID certificatePublicId) {

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
                        institutionId)
                .stream()
                .map(verificationQueryService::buildHistoryView)
                .map(verificationHistoryMapper::toResponse)
                .toList();
    }

    public List<VerificationHistoryResponse> getByInstitution() {

        Long institutionId =
                securityContextService.getInstitutionId();

        return repository
                .findByInstitutionIdOrderByVerifiedAtDesc(
                        institutionId)
                .stream()
                .map(verificationQueryService::buildHistoryView)
                .map(verificationHistoryMapper::toResponse)
                .toList();
    }

    public Page<VerificationHistoryResponse> search(
            VerificationHistoryFilterRequest request,
            Pageable pageable) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Long certificateId = null;

        if (request.getCertificatePublicId() != null) {
            certificateId =
                    certificateQueryService
                            .getCertificate(
                                    request.getCertificatePublicId())
                            .getId();
        }

        return repository.findAll(
                        VerificationSpecification.filter(
                                request,
                                institutionId,
                                certificateId),
                        pageable)

                .map(verificationQueryService::buildHistoryView)
                .map(verificationHistoryMapper::toResponse);
    }
}
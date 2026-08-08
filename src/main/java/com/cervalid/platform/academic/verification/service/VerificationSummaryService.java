package com.cervalid.platform.academic.verification.service;

import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.verification.dto.response.VerificationCertificateSummaryResponse;
import com.cervalid.platform.academic.verification.mapper.VerificationCertificateSummaryMapper;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationSummaryService {

    private final CertificateRepository certificateRepository;
    private final SecurityContextService securityContextService;
    private final VerificationSummaryQueryService queryService;
    private final VerificationCertificateSummaryMapper mapper;

    public Page<VerificationCertificateSummaryResponse> search(
            Pageable pageable) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return certificateRepository

                .findByInstitutionId(institutionId, pageable)
                .map(queryService::build)
                .map(mapper::toResponse);
    }
}
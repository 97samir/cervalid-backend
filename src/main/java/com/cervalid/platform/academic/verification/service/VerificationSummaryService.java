package com.cervalid.platform.academic.verification.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.repository.CredentialRepository;
import com.cervalid.platform.academic.verification.dto.request.VerificationSummaryFilterRequest;
import com.cervalid.platform.academic.verification.dto.response.VerificationSummaryResponse;
import com.cervalid.platform.academic.verification.dto.view.VerificationSummaryView;
import com.cervalid.platform.academic.verification.mapper.VerificationSummaryMapper;
import com.cervalid.platform.academic.verification.specification.VerificationSummarySpecification;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationSummaryService {

    private final CertificateRepository certificateRepository;
    private final CredentialRepository credentialRepository;

    private final SecurityContextService securityContextService;

    private final VerificationSummaryQueryService queryService;
    private final VerificationSummaryMapper mapper;

    public Page<VerificationSummaryResponse> search(
            VerificationSummaryFilterRequest request,
            Pageable pageable) {

        Long institutionId =
                securityContextService.getInstitutionId();

        List<VerificationSummaryView> summaries =
                new ArrayList<>();

        if (shouldIncludeCertificates(request)) {

            List<Certificate> certificates =
                    certificateRepository.findAll(
                            VerificationSummarySpecification
                                    .filterCertificates(
                                            request,
                                            institutionId
                                    )
                    );

            certificates.forEach(certificate ->
                    summaries.add(
                            queryService.buildCertificate(
                                    certificate
                            )
                    )
            );
        }

        if (shouldIncludeCredentials(request)) {

            List<Credential> credentials =
                    credentialRepository.findAll(
                            VerificationSummarySpecification
                                    .filterCredentials(
                                            request,
                                            institutionId
                                    )
                    );

            credentials.forEach(credential ->
                    summaries.add(
                            queryService.buildCredential(
                                    credential
                            )
                    )
            );
        }

        // recursos recientes, aparecen primero
        summaries.sort(
                Comparator.comparing(
                        VerificationSummaryView::getLastVerifiedAt,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )
                )
        );

        // paginacion
        int total = summaries.size();

        int start = (int) pageable.getOffset();

        int end = Math.min(
                start + pageable.getPageSize(),
                total
        );

        List<VerificationSummaryView> pageContent =
                start >= total
                        ? List.of()
                        : summaries.subList(
                        start,
                        end
                );

        List<VerificationSummaryResponse> response =
                pageContent.stream()
                        .map(mapper::toResponse)
                        .toList();

        return new PageImpl<>(
                response,
                pageable,
                total
        );
    }

    private boolean shouldIncludeCertificates(
            VerificationSummaryFilterRequest request) {

        if (request == null) {
            return true;
        }

        if (request.getVerificationType() == null
                || request.getVerificationType().isBlank()) {
            return true;
        }

        return "CERTIFICATE".equalsIgnoreCase(
                request.getVerificationType()
        );
    }

    private boolean shouldIncludeCredentials(
            VerificationSummaryFilterRequest request) {

        if (request == null) {
            return true;
        }

        if (request.getVerificationType() == null
                || request.getVerificationType().isBlank()) {
            return true;
        }

        return "CREDENTIAL".equalsIgnoreCase(
                request.getVerificationType()
        );
    }
}
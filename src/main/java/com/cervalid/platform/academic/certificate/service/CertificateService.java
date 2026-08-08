package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.dto.request.CertificateSearchRequest;
import com.cervalid.platform.academic.certificate.dto.request.IssueCertificateRequest;
import com.cervalid.platform.academic.certificate.dto.response.CertificateDetailResponse;
import com.cervalid.platform.academic.certificate.dto.response.CertificateResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateManagementService managementService;
    private final CertificateQueryService queryService;

    public CertificateResponse issue(
            IssueCertificateRequest request) {

        return managementService.issue(request);
    }

    public CertificateResponse issueFromTranscript(
            UUID transcriptPublicId) {

        return managementService.issueFromTranscript(
                transcriptPublicId);
    }

    public void revoke(
            UUID publicId) {

        managementService.revoke(publicId);
    }

    public Page<CertificateResponse> getByStudent(
            UUID studentPublicId,
            Pageable pageable) {

        return queryService.getByStudent(
                studentPublicId,
                pageable
        );
    }

    public CertificateResponse getByPublicId(
            UUID publicId) {

        return queryService.getByPublicId(publicId);
    }

    public CertificateResponse getByCertificateNumber(
            String number) {

        return queryService.getByCertificateNumber(number);
    }

    public CertificateDetailResponse getDetail(
            UUID publicId) {

        return queryService.getDetail(publicId);
    }

    public Page<CertificateResponse> search(
            CertificateSearchRequest request,
            Pageable pageable) {

        return queryService.search(
                request,
                pageable);
    }

}
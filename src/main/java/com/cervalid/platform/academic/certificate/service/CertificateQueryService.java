package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.dto.request.CertificateSearchRequest;
import com.cervalid.platform.academic.certificate.dto.response.CertificateDetailResponse;
import com.cervalid.platform.academic.certificate.dto.response.CertificateResponse;
import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.mapper.CertificateMapper;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.certificate.specification.CertificateSpecification;
import com.cervalid.platform.academic.certificate.validation.CertificateOwnershipValidator;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificateQueryService {

    private final CertificateRepository repository;
    private final SecurityContextService securityContextService;
    private final CertificateOwnershipValidator certificateOwnershipValidator;
    private final CertificateMapper mapper;

    public Page<CertificateResponse> getByStudent(
            UUID studentPublicId,
            Pageable pageable) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Student student =
                certificateOwnershipValidator
                        .validateStudentOwnership(
                                studentPublicId,
                                institutionId);

        return repository
                .findByStudentIdAndInstitutionId(
                        student.getId(),
                        institutionId,
                        pageable
                )
                .map(mapper::toResponse);
    }

    public CertificateResponse getByPublicId(
            UUID publicId) {

        return mapper.toResponse(
                getCertificate(publicId));
    }

    public CertificateResponse getByCertificateNumber(
            String certificateNumber) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Certificate certificate =
                repository
                        .findByCertificateNumberAndInstitutionId(
                                certificateNumber,
                                institutionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Certificate not found"));

        return mapper.toResponse(
                certificate);
    }

    public CertificateDetailResponse getDetail(
            UUID publicId) {

        return mapper.toResponseDetail(
                getCertificate(publicId));
    }

    public Page<CertificateResponse> search(
            CertificateSearchRequest request,
            Pageable pageable) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return repository.findAll(
                        CertificateSpecification.build(
                                request,
                                institutionId),
                        pageable)
                .map(mapper::toResponse);
    }

    public Certificate getCertificate(
            UUID publicId) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return repository
                .findByPublicIdAndInstitutionId(
                        publicId,
                        institutionId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Certificate not found"));
    }
}
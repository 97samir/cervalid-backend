
package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.dto.request.CertificateSearchRequest;
import com.cervalid.platform.academic.certificate.dto.response.CertificateDashboardSummaryResponse;
import com.cervalid.platform.academic.certificate.dto.response.CertificateDetailResponse;
import com.cervalid.platform.academic.certificate.dto.response.CertificateResponse;
import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.enums.CertificateStatus;
import com.cervalid.platform.academic.certificate.mapper.CertificateMapper;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.certificate.specification.CertificateSpecification;
import com.cervalid.platform.academic.certificate.validation.CertificateOwnershipValidator;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.security.context.SecurityContextService;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CertificateQueryService {

    private final CertificateRepository repository;
    private final SecurityContextService securityContextService;
    private final CertificateOwnershipValidator certificateOwnershipValidator;
    private final CertificateMapper mapper;

    private final StudentRepository studentRepository;
    private final AcademicProfileRepository academicProfileRepository;
    private final UserRepository userRepository;

    public Page<CertificateResponse> getByStudent(
            UUID studentPublicId,
            Pageable pageable
    ) {
        Long institutionId = securityContextService.getInstitutionId();

        Student student = certificateOwnershipValidator
                .validateStudentOwnership(studentPublicId, institutionId);

        Page<Certificate> page =
                repository.findByStudentIdAndInstitutionIdAndDeletedFalse(
                        student.getId(),
                        institutionId,
                        pageable
                );

        return enrichPage(page, institutionId);
    }

    public CertificateResponse getByPublicId(UUID publicId) {
        return getResponseByPublicId(
                securityContextService.getInstitutionId(),
                publicId
        );
    }

    public CertificateResponse getByCertificateNumber(
            String certificateNumber
    ) {
        Long institutionId = securityContextService.getInstitutionId();

        Certificate certificate = repository
                .findByCertificateNumberAndInstitutionId(
                        certificateNumber,
                        institutionId
                )
                .filter(item -> !item.isDeleted())
                .orElseThrow(() ->
                        new RuntimeException("Certificate not found"));

        return getResponseByPublicId(
                institutionId,
                certificate.getPublicId()
        );
    }

    public CertificateDetailResponse getDetail(UUID publicId) {
        Certificate certificate = getCertificate(publicId);

        CertificateResponse response = getResponseByPublicId(
                certificate.getInstitutionId(),
                publicId
        );

        return mapper.toResponseDetail(
                certificate,
                response);
    }

    public Page<CertificateResponse> search(
            CertificateSearchRequest request,
            Pageable pageable
    ) {
        Long institutionId = securityContextService.getInstitutionId();

        Page<Certificate> page = repository.findAll(
                CertificateSpecification.build(request, institutionId),
                pageable
        );

        return enrichPage(page, institutionId);
    }

    public Certificate getCertificate(UUID publicId) {
        Long institutionId = securityContextService.getInstitutionId();

        return repository
                .findByPublicIdAndInstitutionId(publicId, institutionId)
                .filter(certificate -> !certificate.isDeleted())
                .orElseThrow(() ->
                        new RuntimeException("Certificate not found"));
    }

    public CertificateDashboardSummaryResponse getDashboardSummary() {
        Long institutionId = securityContextService.getInstitutionId();

        return CertificateDashboardSummaryResponse.builder()
                .total(repository.countByInstitutionIdAndDeletedFalse(institutionId))
                .issued(repository.countByInstitutionIdAndStatusAndDeletedFalse(
                        institutionId,
                        CertificateStatus.ISSUED
                ))
                .drafts(repository.countByInstitutionIdAndStatusAndDeletedFalse(
                        institutionId,
                        CertificateStatus.DRAFT
                ))
                .revoked(repository.countByInstitutionIdAndStatusAndDeletedFalse(
                        institutionId,
                        CertificateStatus.REVOKED
                ))
                .build();
    }

    public CertificateResponse getResponseByPublicId(
            Long institutionId,
            UUID publicId
    ) {
        Certificate certificate = repository
                .findByPublicIdAndInstitutionId(publicId, institutionId)
                .filter(item -> !item.isDeleted())
                .orElseThrow(() ->
                        new RuntimeException("Certificate not found"));

        return enrichPage(
                new org.springframework.data.domain.PageImpl<>(
                        List.of(certificate)
                ),
                institutionId
        ).getContent().get(0);
    }

    private Page<CertificateResponse> enrichPage(
            Page<Certificate> certificates,
            Long institutionId
    ) {
        List<Certificate> content = certificates.getContent();

        if (content.isEmpty()) {
            return certificates.map(mapper::toResponse);
        }

        Set<Long> studentIds = content.stream()
                .map(Certificate::getStudentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, Student> studentsById = new HashMap<>();

        if (!studentIds.isEmpty()) {
            studentRepository
                    .findAllByInstitutionIdAndIdIn(
                            institutionId,
                            studentIds
                    )
                    .stream()
                    .filter(student -> !student.isDeleted())
                    .forEach(student ->
                            studentsById.put(student.getId(), student));
        }

        Set<Long> userIds = studentsById.values().stream()
                .map(Student::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, User> usersById = userIds.isEmpty()
                ? Map.of()
                : userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> user,
                        (first, second) -> first
                ));

        Map<Long, AcademicProfile> profilesByStudentId =
                studentsById.isEmpty()
                        ? Map.of()
                        : academicProfileRepository
                        .findAllByInstitutionIdAndStudentIdIn(
                                institutionId,
                                studentsById.keySet()
                        )
                        .stream()
                        .collect(Collectors.toMap(
                                AcademicProfile::getStudentId,
                                profile -> profile,
                                (first, second) -> first
                        ));

        return certificates.map(certificate -> {
            Student student = studentsById.get(
                    certificate.getStudentId()
            );

            User user = student != null
                    ? usersById.get(student.getUserId())
                    : null;

            AcademicProfile profile = student != null
                    ? profilesByStudentId.get(student.getId())
                    : null;

            return mapper.toResponse(
                    certificate,
                    student,
                    user,
                    profile
            );
        });
    }
}

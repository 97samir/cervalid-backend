package com.cervalid.platform.academic.competency.service;

import com.cervalid.platform.academic.competency.dto.request.CreateCompetencyRequest;
import com.cervalid.platform.academic.competency.dto.request.UpdateCompetencyRequest;
import com.cervalid.platform.academic.competency.entity.Competency;
import com.cervalid.platform.academic.competency.enums.CompetencySource;
import com.cervalid.platform.academic.competency.enums.CompetencyStatus;
import com.cervalid.platform.academic.competency.repository.CompetencyRepository;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.service.StudentQueryService;
import com.cervalid.platform.security.context.SecurityContextService;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CompetencyManagementService {

    private final CompetencyRepository repository;
    private final StudentQueryService studentQueryService;
    private final CompetencyQueryService queryService;
    private final SecurityContextService securityContextService;
    private final PublicIdGenerator publicIdGenerator;
    private final CompetencyTimelineService timelineService;
    private final CompetencyValidationService validationService;

    public Competency create(
            UUID studentPublicId,
            CreateCompetencyRequest request) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Student student =
                studentQueryService.getByPublicId(
                        studentPublicId);

        if (!student.getInstitutionId()
                .equals(institutionId)) {

            throw new IllegalArgumentException(
                    "Student does not belong to the current institution");
        }

        validationService.validateCreate(
                student,
                request);

        Competency competency =
                Competency.builder()
                        .publicId(publicIdGenerator.generate())
                        .institutionId(institutionId)
                        .studentId(student.getId())
                        .name(request.getName().trim())
                        .description(request.getDescription())
                        .level(request.getLevel())
                        .issuer(normalizeNullable(request.getIssuer()))
                        .acquiredDate(request.getAcquiredDate())
                        .status(CompetencyStatus.ACTIVE)
                        .source(CompetencySource.MANUAL)
                        .evidenceReference(null)
                        .evidenceType(null)
                        .academicPeriod(normalizeNullable(request.getAcademicPeriod()))
                        .build();

        Competency saved = repository.save(competency);

        timelineService.createdManually(saved);

        return saved;
    }

    public Competency update(
            UUID publicId,
            UpdateCompetencyRequest request) {

        Competency competency =
                queryService.getEntityByPublicId(
                        publicId);

        validationService.validateUpdate(
                competency,
                request);

        competency.setName(request.getName().trim());
        competency.setDescription(request.getDescription());
        competency.setLevel(request.getLevel());
        competency.setIssuer(normalizeNullable(request.getIssuer()));
        competency.setAcquiredDate(request.getAcquiredDate());
        competency.setAcademicPeriod(normalizeNullable(request.getAcademicPeriod()));

        Competency saved = repository.save(competency);
        timelineService.updated(saved);

        return saved;
    }

    public void deactivate(
            UUID publicId) {

        Competency competency =
                queryService.getEntityByPublicId(
                        publicId);

        validationService.validateDeactivate(competency);
        competency.setStatus(CompetencyStatus.INACTIVE);
        repository.save(competency);
        timelineService.deactivated(competency);
    }

    private String normalizeNullable(
            String value) {

        if (value == null ||
                value.isBlank()) {

            return null;
        }

        return value.trim();
    }
}
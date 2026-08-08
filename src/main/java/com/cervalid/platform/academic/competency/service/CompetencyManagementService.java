package com.cervalid.platform.academic.competency.service;

import com.cervalid.platform.academic.competency.dto.request.CreateCompetencyRequest;
import com.cervalid.platform.academic.competency.dto.request.UpdateCompetencyRequest;
import com.cervalid.platform.academic.competency.entity.Competency;
import com.cervalid.platform.academic.competency.enums.CompetencySource;
import com.cervalid.platform.academic.competency.enums.CompetencyStatus;
import com.cervalid.platform.academic.competency.repository.CompetencyRepository;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.service.StudentQueryService;
import com.cervalid.platform.academic.timeline.domain.TimelineMetadataBuilder;
import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.cervalid.platform.academic.timeline.service.TimelineEventService;
import com.cervalid.platform.security.context.SecurityContextService;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
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
    private final TimelineEventService timelineEventService;
    private final TimelineMetadataBuilder timelineMetadataBuilder;
    private final CompetencyValidationService validationService;

    public Competency create(
            UUID studentPublicId,
            CreateCompetencyRequest request) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Student student =
                studentQueryService.getByPublicId(
                        studentPublicId);

        validationService.validateCreate(
                student,
                request);

        Competency competency =
                Competency.builder()
                        .publicId(publicIdGenerator.generate())
                        .institutionId(institutionId)
                        .studentId(student.getId())
                        .studentPublicId(student.getPublicId())
                        .name(request.getName())
                        .description(request.getDescription())
                        .level(request.getLevel())
                        .issuer(request.getIssuer())
                        .acquiredDate(request.getAcquiredDate())
                        .status(CompetencyStatus.ACTIVE)
                        .source(CompetencySource.MANUAL)
                        .evidenceReference(null)
                        .evidenceType(null)
                        .build();

        Competency saved = repository.save(competency);

        publishTimeline(
                saved,
                TimelineEventType.COMPETENCY_CREATED,
                "Competency created",
                saved.getName());

        return saved;
    }

    public Competency update(
            UUID publicId,
            UpdateCompetencyRequest request) {

        Competency competency = queryService
                .getEntityByPublicId(publicId);

        validationService.validateUpdate(
                competency,
                request);

        competency.setName(request.getName());
        competency.setDescription(request.getDescription());
        competency.setLevel(request.getLevel());
        competency.setIssuer(request.getIssuer());
        competency.setAcquiredDate(request.getAcquiredDate());

        Competency saved = repository.save(competency);

        publishTimeline(
                saved,
                TimelineEventType.COMPETENCY_UPDATED,
                "Competency updated",
                saved.getName());

        return saved;
    }

    public void deactivate(UUID publicId) {

        Competency competency = queryService
                .getEntityByPublicId(publicId);

        validationService.validateDeactivate(
                competency);

        competency.setStatus(CompetencyStatus.INACTIVE);
        repository.save(competency);

        publishTimeline(
                competency,
                TimelineEventType.COMPETENCY_DEACTIVATED,
                "Competency deactivated",
                competency.getName());
    }

    private void publishTimeline(
            Competency competency,
            TimelineEventType eventType,
            String title,
            String description) {

        JsonNode metadata =
                timelineMetadataBuilder.build(
                        Map.of(
                                "competency", competency.getName(),
                                "level", competency.getLevel().name(),
                                "status", competency.getStatus().name(),
                                "issuer", competency.getIssuer(),
                                "source", competency.getSource().name()
                        ));

        timelineEventService.createEvent(
                competency.getInstitutionId(),
                competency.getStudentId(),
                eventType,
                TimelineEventSource.SYSTEM,
                title,
                description,
                competency.getPublicId(),
                TimelineReferenceType.COMPETENCY,
                metadata
        );
    }

}
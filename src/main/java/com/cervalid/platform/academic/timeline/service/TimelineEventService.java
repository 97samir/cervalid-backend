package com.cervalid.platform.academic.timeline.service;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.timeline.domain.TimelineMetadataBuilder;
import com.cervalid.platform.academic.timeline.dto.request.CreateManualTimelineEventRequest;
import com.cervalid.platform.academic.timeline.dto.response.TimelineEventResponse;
import com.cervalid.platform.academic.timeline.entity.TimelineEvent;
import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.cervalid.platform.academic.timeline.mapper.TimelineEventMapper;
import com.cervalid.platform.academic.timeline.repository.TimelineEventRepository;
import com.cervalid.platform.academic.timeline.validation.TimelineOwnershipValidator;
import com.cervalid.platform.security.context.SecurityContextService;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.cervalid.platform.academic.timeline.enums.TimelineEventType.MANUAL_EVENT;

@Service
@RequiredArgsConstructor
public class TimelineEventService {

    private final TimelineEventRepository repository;
    private final PublicIdGenerator publicIdGenerator;
    private final SecurityContextService securityContextService;
    private final TimelineOwnershipValidator ownershipValidator;
    private final TimelineEventMapper timelineEventMapper;
    private final TimelineMetadataBuilder timelineMetadataBuilder;

    public TimelineEvent createEvent(
            Long institutionId,
            Long studentId,
            TimelineEventType type,
            TimelineEventSource source,
            String title,
            String description,
            UUID referenceId,
            TimelineReferenceType referenceType,
            String academicPeriod,
            JsonNode metadataJson) {

        boolean isSystemEvent = source == TimelineEventSource.SYSTEM;

        if (isSystemEvent && referenceId == null) {
            throw new RuntimeException("System events require referenceId");
        }

        TimelineEvent event = TimelineEvent.builder()
                .publicId(publicIdGenerator.generate())
                .institutionId(institutionId)
                .studentId(studentId)
                .type(type)
                .source(source)
                .title(title)
                .description(description)
                .referenceId(referenceId)
                .referenceType(referenceType)
                .academicPeriod(academicPeriod)
                .metadataJson(metadataJson)
                .eventDate(LocalDateTime.now())
                .build();

        return repository.save(event);
    }

    @Transactional
    public TimelineEventResponse createManualEvent(
            CreateManualTimelineEventRequest request) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Student student =
                ownershipValidator.validateStudentOwnership(
                                request.getStudentPublicId(),
                                institutionId);

        JsonNode metadata =
                timelineMetadataBuilder.build(
                        request.getMetadata());

        TimelineEvent saved = createEvent(
                institutionId,
                student.getId(),
                TimelineEventType.MANUAL_EVENT,
                TimelineEventSource.INSTITUTION_ADMIN,
                request.getTitle(),
                request.getDescription(),
                null, // nul por que es manual
                TimelineReferenceType.MANUAL,
                null,
                metadata
        );

        return timelineEventMapper.toResponse(
                saved,
                request.getStudentPublicId());
    }
}
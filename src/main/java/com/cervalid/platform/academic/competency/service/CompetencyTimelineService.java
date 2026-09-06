package com.cervalid.platform.academic.competency.service;

import com.cervalid.platform.academic.competency.entity.Competency;
import com.cervalid.platform.academic.timeline.domain.TimelineMetadataBuilder;
import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.cervalid.platform.academic.timeline.service.TimelineEventService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompetencyTimelineService {

    private final TimelineEventService timelineEventService;
    private final TimelineMetadataBuilder timelineMetadataBuilder;

    public void createdManually(
            Competency competency) {

        publish(
                competency,
                TimelineEventSource.INSTITUTION_ADMIN,
                TimelineEventType.COMPETENCY_CREATED,
                "Competency created",
                competency.getName());
    }

    public void generatedFromTranscript(
            Competency competency) {

        publish(
                competency,
                TimelineEventSource.SYSTEM,
                TimelineEventType.COMPETENCY_ACQUIRED,
                "Competency acquired",
                competency.getName());
    }

    public void updated(
            Competency competency) {

        publish(
                competency,
                TimelineEventSource.INSTITUTION_ADMIN,
                TimelineEventType.COMPETENCY_UPDATED,
                "Competency updated",
                competency.getName());
    }

    public void deactivated(
            Competency competency) {

        publish(
                competency,
                TimelineEventSource.INSTITUTION_ADMIN,
                TimelineEventType.COMPETENCY_DEACTIVATED,
                "Competency deactivated",
                competency.getName());
    }

    private void publish(
            Competency competency,
            TimelineEventSource source,
            TimelineEventType eventType,
            String title,
            String description) {

        Map<String, Object> values =
                new HashMap<>();

        values.put("competency", competency.getName());
        values.put("level", competency.getLevel().name());
        values.put("status", competency.getStatus().name());
        values.put("source", competency.getSource().name());

        if (competency.getIssuer() != null &&
                !competency.getIssuer().isBlank()) {
            values.put("issuer", competency.getIssuer());
        }

        if (competency.getAcademicPeriod() != null &&
                !competency.getAcademicPeriod().isBlank()) {
            values.put("academicPeriod", competency.getAcademicPeriod());
        }

        JsonNode metadata =
                timelineMetadataBuilder.build(values);

        timelineEventService.createEvent(
                competency.getInstitutionId(),
                competency.getStudentId(),
                eventType,
                source,
                title,
                description,
                competency.getPublicId(),
                TimelineReferenceType.COMPETENCY,
                competency.getAcademicPeriod(),
                metadata);
    }
}
package com.cervalid.platform.academic.achievement.service;

import com.cervalid.platform.academic.achievement.entity.Achievement;
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
public class AchievementTimelineService {

    private final TimelineEventService timelineEventService;
    private final TimelineMetadataBuilder timelineMetadataBuilder;

    public void created(Achievement achievement) {
        publish(
                achievement,
                TimelineEventType.ACHIEVEMENT_EARNED,
                "Achievement earned",
                achievement.getTitle()
        );
    }

    public void updated(Achievement achievement) {
        publish(
                achievement,
                TimelineEventType.ACHIEVEMENT_UPDATED,
                "Achievement updated",
                achievement.getTitle()
        );
    }

    public void deactivated(Achievement achievement) {
        publish(
                achievement,
                TimelineEventType.ACHIEVEMENT_DEACTIVATED,
                "Achievement deactivated",
                achievement.getTitle()
        );
    }

    private void publish(
            Achievement achievement,
            TimelineEventType eventType,
            String title,
            String description) {

        Map<String, Object> metadataValues =
                new HashMap<>();

        metadataValues.put("title", achievement.getTitle());
        metadataValues.put("type", achievement.getType()
                != null
                ? achievement.getType().name()
                : null);
        metadataValues.put("issuer", achievement.getIssuer());
        metadataValues.put("status", achievement.getStatus()
                != null
                ? achievement.getStatus().name()
                : null);
        metadataValues.put("achievedDate", achievement.getAchievedDate()
                != null
                ? achievement.getAchievedDate().toString()
                : null);
        metadataValues.put("academicPeriod", achievement.getAcademicPeriod());

        JsonNode metadata =
                timelineMetadataBuilder.build(
                        metadataValues
                );

        timelineEventService.createEvent(
                achievement.getInstitutionId(),
                achievement.getStudentId(),
                eventType,
                TimelineEventSource.INSTITUTION_ADMIN,
                title,
                description,
                achievement.getPublicId(),
                TimelineReferenceType.ACHIEVEMENT,
                achievement.getAcademicPeriod(),
                metadata
        );
    }
}
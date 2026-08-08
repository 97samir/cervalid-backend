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

        JsonNode metadata =
                timelineMetadataBuilder.build(
                        Map.of(
                                "title", achievement.getTitle(),
                                "type", achievement.getType().name(),
                                "issuer", achievement.getIssuer(),
                                "status", achievement.getStatus().name(),
                                "achievedDate", achievement.getAchievedDate()
                                        != null
                                        ? achievement.getAchievedDate().toString()
                                        : null
                        )
                );

        timelineEventService.createEvent(
                achievement.getInstitutionId(),
                achievement.getStudentId(),
                eventType,
                TimelineEventSource.SYSTEM,
                title,
                description,
                achievement.getPublicId(),
                TimelineReferenceType.ACHIEVEMENT,
                metadata
        );
    }
}
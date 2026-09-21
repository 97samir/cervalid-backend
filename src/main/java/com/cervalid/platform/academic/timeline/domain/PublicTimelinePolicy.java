package com.cervalid.platform.academic.timeline.domain;

import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
public class PublicTimelinePolicy {

    private static final Set<TimelineEventType> PUBLIC_EVENTS =
            EnumSet.of(

                    TimelineEventType.STUDENT_REGISTERED,

                    TimelineEventType.PROFILE_CREATED,
                    //TimelineEventType.PROFILE_UPDATED,

                    TimelineEventType.TRANSCRIPT_FINALIZED,
                    TimelineEventType.TRANSCRIPT_ISSUED,

                    TimelineEventType.CERTIFICATE_ISSUED,
                    TimelineEventType.CERTIFICATE_REVOKED,

                    TimelineEventType.SKILL_EARNED,
                    TimelineEventType.COMPETENCY_ACQUIRED,
                    //TimelineEventType.COMPETENCY_CREATED,
                    //TimelineEventType.COMPETENCY_UPDATED,

                    TimelineEventType.ACHIEVEMENT_EARNED,
                    //TimelineEventType.ACHIEVEMENT_UPDATED,

                    TimelineEventType.CREDENTIAL_ISSUED,
                    TimelineEventType.CREDENTIAL_REVOKED,

                    TimelineEventType.BADGE_GRANTED,

                    TimelineEventType.MANUAL_EVENT
            );

    public boolean isPublic(TimelineEventType type) {
        return type != null && PUBLIC_EVENTS.contains(type);
    }
}
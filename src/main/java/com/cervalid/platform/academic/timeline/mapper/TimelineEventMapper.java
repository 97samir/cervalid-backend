package com.cervalid.platform.academic.timeline.mapper;

import com.cervalid.platform.academic.timeline.dto.response.TimelineEventResponse;
import com.cervalid.platform.academic.timeline.entity.TimelineEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TimelineEventMapper {

    public TimelineEventResponse toResponse(
            TimelineEvent event,
            UUID studentPublicId) {

        return TimelineEventResponse.builder()
                .publicId(event.getPublicId())
                .studentPublicId(studentPublicId)
                .type(event.getType())
                .source(event.getSource())
                .title(event.getTitle())
                .description(event.getDescription())
                .referenceId(event.getReferenceId())
                .referenceType(event.getReferenceType())
                .academicPeriod(event.getAcademicPeriod())
                .eventDate(event.getEventDate())
                .metadataJson(event.getMetadataJson())
                .build();
    }
}
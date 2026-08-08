package com.cervalid.platform.academic.timeline.mapper;

import com.cervalid.platform.academic.student.repository.StudentRepository;
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
                //.institutionId(event.getInstitutionId())
                //.studentId(event.getStudentId())
                .studentPublicId(studentPublicId)
                .type(event.getType())
                .source(event.getSource())
                .title(event.getTitle())
                .description(event.getDescription())
                .referenceId(event.getReferenceId())
                .referenceType(event.getReferenceType() != null
                                ? event.getReferenceType()
                                : null)
                .eventDate(event.getEventDate())
                .metadataJson(event.getMetadataJson())
                .build();
    }
}
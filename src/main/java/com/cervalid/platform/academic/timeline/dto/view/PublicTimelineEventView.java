package com.cervalid.platform.academic.timeline.dto.view;

import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PublicTimelineEventView {

    private UUID publicId;
    private TimelineEventType type;
    //private TimelineEventSource source;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private TimelineReferenceType referenceType;
    private String academicPeriod;

    private JsonNode metadataJson;

}
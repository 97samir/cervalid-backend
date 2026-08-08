package com.cervalid.platform.academic.timeline.dto.response;

import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TimelineEventResponse {

    private UUID publicId;
    //private Long institutionId;
    private UUID studentPublicId;
    private TimelineEventType type;
    private TimelineEventSource source;
    private String title;
    private String description;
    private UUID referenceId;
    private TimelineReferenceType referenceType;
    private LocalDateTime eventDate;
    private JsonNode metadataJson;

}
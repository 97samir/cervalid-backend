package com.cervalid.platform.academic.timeline.dto.request;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class CreateManualTimelineEventRequest {

    //private Long studentId;
    private UUID studentPublicId;
    private String title;
    private String description;
    private Map<String,Object> metadata;
}

package com.cervalid.platform.bulk.event.dto;

import com.cervalid.platform.bulk.event.enums.AcademicEventType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class AcademicEventRequest {

    private Long academicProfileId;
    private AcademicEventType type;
    private LocalDateTime date;
    private String title;
    private String description;
    private Map<String, Object> metadata;
    //private JsonNode metadataJson;

}
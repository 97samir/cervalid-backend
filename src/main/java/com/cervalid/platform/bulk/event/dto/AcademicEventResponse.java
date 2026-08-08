package com.cervalid.platform.bulk.event.dto;

import com.cervalid.platform.bulk.event.enums.AcademicEventType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class AcademicEventResponse {

    private Long id;
    private AcademicEventType type;
    private LocalDateTime date;
    private String title;
    private String description;
    private Map<String, Object> metadataJson;
    //private String metadataJson;
}
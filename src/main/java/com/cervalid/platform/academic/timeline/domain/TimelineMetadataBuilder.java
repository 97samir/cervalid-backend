package com.cervalid.platform.academic.timeline.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TimelineMetadataBuilder {

    private final ObjectMapper objectMapper;

    public JsonNode build(Map<String, Object> values) {
        return objectMapper.valueToTree(values);
    }
}
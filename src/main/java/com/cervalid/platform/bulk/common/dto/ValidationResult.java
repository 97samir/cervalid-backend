package com.cervalid.platform.bulk.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ValidationResult {

    private boolean valid;

    @Builder.Default
    private List<String> errors = new ArrayList<>();
}
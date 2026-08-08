package com.cervalid.platform.academic.profile.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AcademicProfileResponse {

    private UUID publicId;
    private String program;
    private String faculty;
    private String modality;
    private Integer currentCycle;
    private String advisor;
    private Boolean active;
}
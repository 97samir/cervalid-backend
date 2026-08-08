package com.cervalid.platform.academic.profile.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicProfileRequest {

    private String program;
    private String faculty;
    private String modality;
    private Integer currentCycle;
    private String advisor; // tutor
}
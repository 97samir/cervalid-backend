package com.cervalid.platform.academic.profile.dto.internal;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAcademicProfileCommand {

    private Long studentId;
    private Long institutionId;
    private String program;
    private String faculty;
    private String modality;
    private Integer currentCycle;
    private String advisor;

}
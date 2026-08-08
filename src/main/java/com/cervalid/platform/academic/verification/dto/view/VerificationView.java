package com.cervalid.platform.academic.verification.dto.view;

import lombok.*;

@Getter
@Builder
public class VerificationView {

    private String studentName;
    private String institutionName;

    private String program;
    private String faculty;
    private String modality;
    private Integer currentCycle;
}
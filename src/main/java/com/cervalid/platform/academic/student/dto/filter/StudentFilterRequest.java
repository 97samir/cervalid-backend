package com.cervalid.platform.academic.student.dto.filter;

import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentFilterRequest {

    private String studentCode;
    private String status;
    private AcademicProgram program;
    //private Long institutionId;
}
package com.cervalid.platform.academic.student.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentFilterRequest {

    private String studentCode;
    private String status;
    //private Long institutionId;
}
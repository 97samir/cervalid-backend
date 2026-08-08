package com.cervalid.platform.academic.student.dto.request;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequest {

    //private Long userId;
    //private Long institutionId;
    //private Long institutionMembershipId;
    private String email;
    private String studentCode;
    private LocalDate admissionDate;
    private LocalDate graduationDate;
}
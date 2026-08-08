package com.cervalid.platform.academic.student.dto.internal;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentCommand {

    private Long userId;
    private Long institutionId;
    private Long institutionMembershipId;
    private String studentCode;
    private LocalDate admissionDate;
    private LocalDate graduationDate;
}
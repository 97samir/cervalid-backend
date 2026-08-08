package com.cervalid.platform.academic.student.dto.request;

import com.cervalid.platform.academic.student.enums.StudentStatus;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStudentRequest {

    private StudentStatus status;
    private LocalDate graduationDate;
}
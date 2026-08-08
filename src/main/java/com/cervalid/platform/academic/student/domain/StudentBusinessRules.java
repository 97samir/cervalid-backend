package com.cervalid.platform.academic.student.domain;

import com.cervalid.platform.common.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StudentBusinessRules {

    public void validateAdmissionDate(LocalDate admissionDate) {

        if (admissionDate == null) {
            throw new BadRequestException(
                    "Admission date is required"
            );
        }

        if (admissionDate.isAfter(LocalDate.now())) {
            throw new BadRequestException(
                    "Admission date cannot be in the future"
            );
        }
    }
}
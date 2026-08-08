package com.cervalid.platform.academic.student.domain;

import com.cervalid.platform.common.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class StudentCodeValidator {

    public void validate(String studentCode) {

        if (studentCode == null || studentCode.isBlank()) {
            throw new BadRequestException(
                    "Student code is required"
            );
        }

        if (studentCode.length() < 4) {
            throw new BadRequestException(
                    "Invalid student code"
            );
        }

        if (studentCode.length() > 50) {
            throw new BadRequestException(
                    "Student code too long"
            );
        }
    }
}
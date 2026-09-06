package com.cervalid.platform.academic.transcript.validation;

import com.cervalid.platform.academic.transcript.enums.AcademicPeriodType;
import org.springframework.stereotype.Component;

@Component
public class AcademicPeriodValidator {

    public void validate(
            AcademicPeriodType type,
            String period) {

        if (type == null || period == null || period.isBlank()) {
            throw new RuntimeException(
                    "Academic period is required");
        }

        switch (type) {

            case YEAR -> validateYear(period);

            case
                    CYCLE,
                    SEMESTER,
                    QUARTER,
                    TERM ->
                    validateAcademicPeriod(period);
        }
    }

    private void validateYear(String period) {

        if (!period.matches("\\d{4}")) {
            throw new RuntimeException(
                    "Invalid academic year");
        }
    }

    private void validateAcademicPeriod(String period) {

        if (!period.matches("\\d{4}-[A-Za-z0-9]+")) {
            throw new RuntimeException(
                    "Invalid academic period format");
        }
    }
}
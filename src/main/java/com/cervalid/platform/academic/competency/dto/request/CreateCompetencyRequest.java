package com.cervalid.platform.academic.competency.dto.request;

import com.cervalid.platform.academic.competency.enums.CompetencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateCompetencyRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private CompetencyLevel level;

    private String issuer; // por quien fue emitido

    private LocalDate acquiredDate;

    private String academicPeriod;
}
package com.cervalid.platform.academic.competency.dto.request;

import com.cervalid.platform.academic.competency.enums.CompetencyLevel;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class CreateCompetencyRequest {

    //private Long studentId;
    private String name;
    private String description;
    private CompetencyLevel level;
    private String issuer; // quien la otorgo o certifico: Cisco,Oracle,Microsoft,AWS,Google
    private LocalDate acquiredDate; // fecha en que fue emitida
    //private UUID evidenceReference;
    //private String evidenceType;
}
package com.cervalid.platform.academic.competency.dto.response;

import com.cervalid.platform.academic.competency.enums.CompetencyEvidenceType;
import com.cervalid.platform.academic.competency.enums.CompetencyLevel;
import com.cervalid.platform.academic.competency.enums.CompetencySource;
import com.cervalid.platform.academic.competency.enums.CompetencyStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CompetencyResponse {

    private UUID publicId;
    private UUID studentPublicId;
    //private Long institutionId;
    private String name;
    private String description;
    private CompetencyLevel level;
    private CompetencyStatus status;
    private CompetencySource source;
    private String issuer;
    private LocalDate acquiredDate;
    private UUID evidenceReference;
    private CompetencyEvidenceType evidenceType;
}
package com.cervalid.platform.academic.achievement.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AchievementResponse {

    private UUID publicId;
    private UUID studentPublicId;
    private Long institutionId;
    private String title;
    private String description;
    private String type;
    private String issuer;
    private LocalDate achievedDate;
    private String status;
}
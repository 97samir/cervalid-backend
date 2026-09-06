package com.cervalid.platform.academic.achievement.dto.request;

import com.cervalid.platform.academic.achievement.enums.AchievementType;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CreateAchievementRequest {

    private UUID studentPublicId;
    private String title;
    private String description;
    private AchievementType type;
    private String issuer;
    private LocalDate achievedDate;
    private String academicPeriod;
}
package com.cervalid.platform.academic.achievement.mapper;

import com.cervalid.platform.academic.achievement.dto.response.AchievementResponse;
import com.cervalid.platform.academic.achievement.entity.Achievement;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AchievementMapper {

    public AchievementResponse toResponse(
            Achievement achievement,
            UUID studentPublicId) {

        return AchievementResponse.builder()
                .publicId(achievement.getPublicId())
                .institutionId(achievement.getInstitutionId())
                .studentPublicId(studentPublicId)
                .title(achievement.getTitle())
                .description(achievement.getDescription())
                .type(achievement.getType().name())
                .issuer(achievement.getIssuer())
                .achievedDate(achievement.getAchievedDate())
                .status(achievement.getStatus().name())
                .build();
    }
}
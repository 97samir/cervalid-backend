package com.cervalid.platform.academic.achievement.enums;

public enum AchievementStatus {

    ACTIVE,
    INACTIVE,
    ARCHIVED;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isInactive() {
        return this == INACTIVE;
    }

    public boolean isArchived() {
        return this == ARCHIVED;
    }

}
package com.cervalid.platform.academic.competency.enums;

public enum CompetencyStatus {

    ACTIVE,
    INACTIVE,
    REVOKED,
    EXPIRED;

    public boolean isInactive() {
        return this == INACTIVE;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isRevoked() {
        return this == REVOKED;
    }
}
package com.cervalid.platform.academic.verification.enums;

public enum VerificationLevel {

    LOCAL,                // existe en BD
    HASH_VALIDATED,      // hash coincide
    BLOCKCHAIN_VALIDATED // hash + blockchain coincide
}
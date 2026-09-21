package com.cervalid.platform.academic.credential.dto.view;

import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.enums.CredentialType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public interface CredentialListView {

    UUID getCredentialPublicId();
    UUID getStudentPublicId();
    CredentialType getType();
    String getTitle();
    LocalDate getAwardedAt();
    LocalDateTime getIssuedAt();
    CredentialStatus getStatus();
}
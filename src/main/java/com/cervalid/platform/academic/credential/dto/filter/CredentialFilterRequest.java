package com.cervalid.platform.academic.credential.dto.filter;

import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.enums.CredentialType;
import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialFilterRequest {

    private UUID studentPublicId;
    private CredentialType type;
    private CredentialStatus status;
    private String credentialNumber;
    private String title;

    // filtros de busqueda
    private String search; // por numero de credential o titulo
    private String studentCode;
    private AcademicProgram program;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
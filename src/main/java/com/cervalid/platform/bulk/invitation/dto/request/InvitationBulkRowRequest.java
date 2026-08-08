package com.cervalid.platform.bulk.invitation.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class InvitationBulkRowRequest {

    private Integer rowNumber;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private String role;
}
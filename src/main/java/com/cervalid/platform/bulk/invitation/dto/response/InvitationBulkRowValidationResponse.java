package com.cervalid.platform.bulk.invitation.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
public class InvitationBulkRowValidationResponse {

    private Integer rowNumber;
    private String email;
    private String role;
    private boolean valid;
    private List<String> errors;
}

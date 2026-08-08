package com.cervalid.platform.bulk.invitation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvitationBulkItemResponse {

    private String email;
    private boolean success;
    private String message;
}
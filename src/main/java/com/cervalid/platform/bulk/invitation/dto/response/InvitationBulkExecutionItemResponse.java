package com.cervalid.platform.bulk.invitation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvitationBulkExecutionItemResponse {

    private Integer rowNumber;
    private String email;
    private boolean success;
    private String message;
    private List<String> errors;
}
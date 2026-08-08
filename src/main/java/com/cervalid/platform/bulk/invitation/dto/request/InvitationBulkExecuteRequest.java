package com.cervalid.platform.bulk.invitation.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class InvitationBulkExecuteRequest {

    private List<InvitationBulkRowRequest> rows;
    private Long institutionId;
}
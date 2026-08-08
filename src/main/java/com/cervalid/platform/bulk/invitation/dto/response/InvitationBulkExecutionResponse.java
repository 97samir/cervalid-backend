package com.cervalid.platform.bulk.invitation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvitationBulkExecutionResponse {

    private Integer totalProcessed;
    private Integer successCount;
    private Integer failedCount;
    private List<InvitationBulkExecutionItemResponse> results;
}
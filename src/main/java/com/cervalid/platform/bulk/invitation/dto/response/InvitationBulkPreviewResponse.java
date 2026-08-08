package com.cervalid.platform.bulk.invitation.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
public class InvitationBulkPreviewResponse {

    private Integer totalRows;
    private Integer validRows;
    private Integer invalidRows;

    private List<String> unknownColumns;
    private List<String> missingRequiredColumns;

    private List<InvitationBulkPreviewRowResponse> rows;
}

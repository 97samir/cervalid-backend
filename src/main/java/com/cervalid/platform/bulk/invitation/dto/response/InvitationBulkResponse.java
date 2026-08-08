package com.cervalid.platform.bulk.invitation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InvitationBulkResponse {

    private int total;
    private int success;
    private int failed;
    private List<InvitationBulkItemResponse> results;
}
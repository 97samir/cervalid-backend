package com.cervalid.platform.bulk.job.dto;

import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRowRequest;
import lombok.Data;

import java.util.List;

@Data
public class BulkJobCreateRequest {

    private String jobType;
    private List<InvitationBulkRowRequest> rows;
}
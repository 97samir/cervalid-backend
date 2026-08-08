package com.cervalid.platform.bulk.invitation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class InvitationBulkRequest {

    @Valid
    @NotEmpty
    private List<InvitationBulkRowRequest> rows;
}
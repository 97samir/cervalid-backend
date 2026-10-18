package com.cervalid.platform.academic.credential.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class CredentialDashboardSummaryResponse {

    private long total;
    private long issued;
    private long drafts;
    private long revoked;
}


package com.cervalid.platform.academic.certificate.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDashboardSummaryResponse {

    private long total;
    private long issued;
    private long drafts;
    private long revoked;
}

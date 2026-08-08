package com.cervalid.platform.academic.verification.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerificationDashboardResponse {

    // general
    private long totalVerifications;
    private long uniqueCertificates;

    // Time
    private long today;
    private long last7Days;
    private long last30Days;

    // estado
    private long validVerifications;
    private long invalidVerifications;
    private long revokedVerifications;
    private long notFoundVerifications;

    // porcentaje
    private double validRate;
    private double invalidRate;
}
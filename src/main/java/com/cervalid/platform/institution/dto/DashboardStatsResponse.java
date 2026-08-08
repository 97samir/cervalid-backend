package com.cervalid.platform.institution.dto;

import lombok.*;

@Setter
@Getter
@Builder
public class DashboardStatsResponse {

    private Long totalUsers;
    private Long totalStudents;
    private Long totalAdmins;

    // futuro
    private Long totalCertificates;
    private Long totalVerified;

}

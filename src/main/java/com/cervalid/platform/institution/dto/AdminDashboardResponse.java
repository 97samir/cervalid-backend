package com.cervalid.platform.institution.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
public class AdminDashboardResponse {

    private Long totalInstitutions;
    private Long activeInstitutions;
    private Long pendingRequests;
    private Long totalUsers;
}
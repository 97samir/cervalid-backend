package com.cervalid.platform.tenant.dto;

import com.cervalid.platform.tenant.enums.RequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ApproveInstitutionResponse {

    private Long institutionId;
    private String institutionName;
    private String walletAddress;
    private boolean approved;
    private String blockchainTxHash;
    private RequestStatus status;
    private String message;
}
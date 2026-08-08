package com.cervalid.platform.academic.verification.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCertificateRequest {

    private String certificateNumber;
    private String hash;

}
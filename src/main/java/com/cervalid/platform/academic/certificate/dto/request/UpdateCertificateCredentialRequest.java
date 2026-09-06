package com.cervalid.platform.academic.certificate.dto.request;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCertificateCredentialRequest {

    private String title;
    private LocalDate awardedAt;
}
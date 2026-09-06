package com.cervalid.platform.academic.certificate.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCertificateDocumentRequest {

    private String documentHash;
    private String documentUrl;
}
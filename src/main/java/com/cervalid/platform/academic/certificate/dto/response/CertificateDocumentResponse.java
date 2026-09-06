package com.cervalid.platform.academic.certificate.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDocumentResponse {

    private String certificatePublicId;
    private String documentHash;
    private String documentUrl;
}
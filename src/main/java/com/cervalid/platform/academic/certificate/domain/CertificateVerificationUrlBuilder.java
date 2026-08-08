package com.cervalid.platform.academic.certificate.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CertificateVerificationUrlBuilder {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.certificate.verification-url}")
    private String verificationPath;

    public String build(UUID publicId) {

        return frontendUrl
                + verificationPath
                + "/"
                + publicId;
    }
}
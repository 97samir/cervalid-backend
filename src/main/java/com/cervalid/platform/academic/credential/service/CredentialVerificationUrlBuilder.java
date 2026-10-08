package com.cervalid.platform.academic.credential.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CredentialVerificationUrlBuilder {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.credential.verification-url}")
    private String verificationPath;

    public String build(UUID publicId) {

        return frontendUrl
                + verificationPath
                + "/"
                + publicId;
    }
}
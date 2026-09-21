package com.cervalid.platform.academic.credential.service;

import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.UUID;

@Component
public class CredentialNumberGenerator {

    public String generate() {
        return "CV-"
                + Year.now().getValue()
                + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();
    }
}
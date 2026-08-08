package com.cervalid.platform.academic.certificate.service;

import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.UUID;

@Component
public class CertificateNumberGenerator {

    public String generate() {

        return "CERT-"
                + Year.now().getValue()
                + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0,8)
                .toUpperCase();
    }
}
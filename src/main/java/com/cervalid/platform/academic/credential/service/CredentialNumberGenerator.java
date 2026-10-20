package com.cervalid.platform.academic.credential.service;

import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.UUID;

@Component
public class CredentialNumberGenerator {

    // obtener un cod identificador de la institucion en el request
    // el año debe proceder de la fecha real en que fue otorgado
    // ejemplo: UTP-CRED-26-A7F29C41B82D

    public String generate() {
        return "CRED-"
                + Year.now().getValue()
                + "-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                // cambiar 8 por 12 bits, menos probabilidad de coincidencia
                .toUpperCase();
    }
}
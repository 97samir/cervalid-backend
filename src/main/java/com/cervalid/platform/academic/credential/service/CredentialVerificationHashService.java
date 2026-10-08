package com.cervalid.platform.academic.credential.service;

import com.cervalid.platform.academic.verification.proof.VerificationProofService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialVerificationHashService {

    private final VerificationProofService verificationProofService;

    public String generateVerificationHash(
            String credentialHash) {

        if (credentialHash == null
                || credentialHash.isBlank()) {

            throw new IllegalArgumentException(
                    "El credentialHash es obligatorio"
            );
        }

        return verificationProofService
                .generateCredentialProof(
                        credentialHash
                );
    }
}
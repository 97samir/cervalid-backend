package com.cervalid.platform.blockchain.adapter;

import com.cervalid.platform.blockchain.dto.BlockchainAnchorResult;
import com.cervalid.platform.blockchain.service.BlockchainService;
import com.cervalid.platform.shared.hashing.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockchainAnchorService {

    private final BlockchainService blockchainService;
    private final HashService hashService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    // CERTIFICATES
    public BlockchainAnchorResult anchorCertificate(
            BlockchainCertificatePayload payload
    ) {
        String payloadHash = hashService.sha256(payload.toMap());

        return blockchainService.registerCertificate(
                payloadHash,
                frontendUrl
                        + "/metadata/"
                        + payload.getCertificateNumber()
        );
    }

    // CREDENTIALS
    public BlockchainAnchorResult anchorCredential(
            String credentialHash,
            String verificationUrl
    ) {
        if (credentialHash == null || credentialHash.isBlank()) {
            throw new IllegalArgumentException(
                    "El hash de la credencial es obligatorio"
            );
        }

        if (verificationUrl == null || verificationUrl.isBlank()) {
            throw new IllegalArgumentException(
                    "La URL de verificación es obligatoria"
            );
        }

        // El credentialHash ya fue calculado por CredentialHashService.
        // No volver a calcularlo: se registra directamente en blockchain.
        return blockchainService.registerCredential(
                credentialHash,
                verificationUrl
        );
    }

    public BlockchainAnchorResult revokeCredential(
            String credentialHash
    ) {
        return blockchainService.revokeCredential(credentialHash);
    }

    public boolean verifyCredential(String credentialHash) {
        return blockchainService.verifyCredential(credentialHash);
    }
}
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

    public BlockchainAnchorResult anchorCertificate(
            BlockchainCertificatePayload payload) {

        // Convertir payload a JSON determinístico hashable
        String payloadHash = hashService.sha256(payload.toMap());

        // Enviar SOLO hash a blockchain
        return blockchainService.registerCertificate(
                payloadHash,
                frontendUrl +
                        "/metadata/" +
                        payload.getCertificateNumber()
        );
    }
}
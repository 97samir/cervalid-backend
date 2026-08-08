package com.cervalid.platform.blockchain.service;

import com.cervalid.platform.blockchain.dto.BlockchainAnchorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlockchainApplicationService {

    private final BlockchainService blockchainService;

    public BlockchainAnchorResult anchorCertificate(String certificateHash) {

        // lógica de negocio
        String metadataURI = buildMetadataURI(certificateHash);

        return blockchainService.registerCertificate(
                certificateHash, metadataURI
        );
    }

    public boolean verify(String certificateHash) {
        return blockchainService
                .verifyCertificate(certificateHash);
    }

    private String buildMetadataURI(String hash) {
        return "https://cervalid.com/metadata/" + hash;
    }
}

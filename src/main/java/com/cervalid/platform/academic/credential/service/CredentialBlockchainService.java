package com.cervalid.platform.academic.credential.service;

import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.blockchain.adapter.BlockchainAnchorService;
import com.cervalid.platform.blockchain.dto.BlockchainAnchorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CredentialBlockchainService {

    private final BlockchainAnchorService blockchainAnchorService;

    public BlockchainAnchorResult anchor(Credential credential) {
        if (credential == null) {
            throw new IllegalArgumentException(
                    "La credencial no puede ser null"
            );
        }

        try {
            BlockchainAnchorResult result =
                    blockchainAnchorService.anchorCredential(
                            credential.getCredentialHash(),
                            credential.getVerificationUrl()
                    );

            log.info(
                    "Credencial anclada. PublicId: {}, TX: {}",
                    credential.getPublicId(),
                    result.getTxHash()
            );

            return result;

        } catch (Exception e) {
            log.error(
                    "Error al anclar la credencial {}",
                    credential.getPublicId(),
                    e
            );

            throw new IllegalStateException(
                    "No se pudo registrar la credencial en blockchain",
                    e
            );
        }
    }

    public BlockchainAnchorResult revoke(Credential credential) {
        if (credential == null) {
            throw new IllegalArgumentException(
                    "La credencial no puede ser null"
            );
        }

        try {
            BlockchainAnchorResult result =
                    blockchainAnchorService.revokeCredential(
                            credential.getCredentialHash()
                    );

            log.info(
                    "Credencial revocada. PublicId: {}, TX: {}",
                    credential.getPublicId(),
                    result.getTxHash()
            );

            return result;

        } catch (Exception e) {
            log.error(
                    "Error al revocar la credencial {}",
                    credential.getPublicId(),
                    e
            );

            throw new IllegalStateException(
                    "No se pudo revocar la credencial en blockchain",
                    e
            );
        }
    }

    public boolean verify(Credential credential) {
        if (credential == null
                || credential.getCredentialHash() == null
                || credential.getCredentialHash().isBlank()) {
            return false;
        }

        return blockchainAnchorService.verifyCredential(
                credential.getCredentialHash()
        );
    }
}
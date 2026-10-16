package com.cervalid.platform.blockchain.service;

import com.cervalid.platform.blockchain.contract.DocumentRegistry;
import com.cervalid.platform.blockchain.dto.BlockchainAnchorResult;
import com.cervalid.platform.blockchain.mapper.BlockchainMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainService {

    private static final BigInteger CERTIFICATE_TYPE = BigInteger.ZERO;
    private static final BigInteger CREDENTIAL_TYPE = BigInteger.ONE;

    private final BlockchainContractFactory blockchainContractFactory;
    private final BlockchainMapper blockchainMapper;

    @Value("${blockchain.network}")
    private String network;

    // CERTIFICATES
    public BlockchainAnchorResult registerCertificate(
            String hash,
            String metadataURI
    ) {
        return registerDocument(
                hash,
                CERTIFICATE_TYPE,
                metadataURI,
                "registrar el certificado"
        );
    }

    public boolean verifyCertificate(String hash) {
        return verifyDocument(
                hash,
                CERTIFICATE_TYPE,
                "certificado"
        );
    }

    public BlockchainAnchorResult revokeCertificate(String hash) {
        return revokeDocument(
                hash,
                CERTIFICATE_TYPE,
                "revocar el certificado"
        );
    }

    // CREDENTIALS
    public BlockchainAnchorResult registerCredential(
            String hash,
            String metadataURI
    ) {
        return registerDocument(
                hash,
                CREDENTIAL_TYPE,
                metadataURI,
                "registrar la credencial"
        );
    }

    public boolean verifyCredential(String hash) {
        return verifyDocument(
                hash,
                CREDENTIAL_TYPE,
                "credencial"
        );
    }

    public BlockchainAnchorResult revokeCredential(String hash) {
        return revokeDocument(
                hash,
                CREDENTIAL_TYPE,
                "revocar la credencial"
        );
    }

    // SHARED DOCUMENT OPERATIONS
    private BlockchainAnchorResult registerDocument(
            String hash,
            BigInteger documentType,
            String metadataURI,
            String operation
    ) {
        try {
            if (metadataURI == null || metadataURI.isBlank()) {
                throw new IllegalArgumentException(
                        "La URL de metadatos es obligatoria"
                );
            }

            DocumentRegistry contract =
                    blockchainContractFactory.loadDocumentRegistry();

            TransactionReceipt receipt = contract.registerDocument(
                    hexStringToBytes32(hash),
                    documentType,
                    metadataURI
            ).send();

            validateReceipt(receipt, operation);

            return blockchainMapper.toResult(receipt, network);

        } catch (Exception e) {
            log.error("Error al {}", operation, e);
            throw new RuntimeException(
                    "Error al " + operation + " en blockchain",
                    e
            );
        }
    }

    private boolean verifyDocument(
            String hash,
            BigInteger documentType,
            String documentName
    ) {
        try {
            DocumentRegistry contract =
                    blockchainContractFactory.loadDocumentRegistry();

            Boolean result = contract.verifyDocument(
                    hexStringToBytes32(hash),
                    documentType
            ).send();

            return Boolean.TRUE.equals(result);

        } catch (Exception e) {
            log.error(
                    "Error al verificar el {} en blockchain",
                    documentName,
                    e
            );
            return false;
        }
    }

    private BlockchainAnchorResult revokeDocument(
            String hash,
            BigInteger documentType,
            String operation
    ) {
        try {
            DocumentRegistry contract =
                    blockchainContractFactory.loadDocumentRegistry();

            TransactionReceipt receipt = contract.revokeDocument(
                    hexStringToBytes32(hash),
                    documentType
            ).send();

            validateReceipt(receipt, operation);

            return blockchainMapper.toResult(receipt, network);

        } catch (Exception e) {
            log.error("Error al {}", operation, e);
            throw new RuntimeException(
                    "Error al " + operation + " en blockchain",
                    e
            );
        }
    }

    // UTILITIES
    private void validateReceipt(
            TransactionReceipt receipt,
            String operation
    ) {
        if (receipt == null || !receipt.isStatusOK()) {
            String status = receipt == null
                    ? "sin recibo"
                    : receipt.getStatus();

            throw new IllegalStateException(
                    "No se pudo " + operation
                            + ". Estado de la transacción: " + status
            );
        }
    }

    private byte[] hexStringToBytes32(String hash) {
        if (hash == null || hash.isBlank()) {
            throw new IllegalArgumentException(
                    "El hash no puede estar vacío"
            );
        }

        String normalizedHash = hash.startsWith("0x")
                ? hash.substring(2)
                : hash;

        if (!normalizedHash.matches("[0-9a-fA-F]{64}")) {
            throw new IllegalArgumentException(
                    "El hash debe contener exactamente 32 bytes "
                            + "(64 caracteres hexadecimales)"
            );
        }

        return Numeric.hexStringToByteArray(normalizedHash);
    }
}
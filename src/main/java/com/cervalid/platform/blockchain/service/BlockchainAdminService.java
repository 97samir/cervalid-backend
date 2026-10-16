package com.cervalid.platform.blockchain.service;

import com.cervalid.platform.blockchain.contract.DocumentRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainAdminService {

    private final BlockchainContractFactory blockchainContractFactory;

    public String registerInstitution(String walletAddress) {
        try {
            validateWalletAddress(walletAddress);

            DocumentRegistry contract =
                    blockchainContractFactory.loadDocumentRegistry();

            log.info(
                    "Registrando institución en DocumentRegistry: {}",
                    walletAddress
            );

            TransactionReceipt receipt = contract
                    .addInstitution(walletAddress)
                    .send();

            validateReceipt(receipt, "registrar la institución");

            String txHash = receipt.getTransactionHash();

            log.info(
                    "Institución registrada en blockchain. TX: {}",
                    txHash
            );

            return txHash;

        } catch (Exception e) {
            log.error(
                    "Error al registrar la institución en blockchain: {}",
                    walletAddress,
                    e
            );

            throw new RuntimeException(
                    "Error al registrar la institución en blockchain",
                    e
            );
        }
    }

    public boolean isInstitution(String walletAddress) {
        try {
            validateWalletAddress(walletAddress);

            DocumentRegistry contract =
                    blockchainContractFactory.loadDocumentRegistry();

            return Boolean.TRUE.equals(
                    contract.isInstitution(walletAddress).send()
            );

        } catch (Exception e) {
            log.error(
                    "Error al comprobar el rol de institución: {}",
                    walletAddress,
                    e
            );

            throw new RuntimeException(
                    "Error al comprobar el rol de institución en blockchain",
                    e
            );
        }
    }

    private void validateWalletAddress(String walletAddress) {
        if (walletAddress == null
                || !walletAddress.matches("0x[a-fA-F0-9]{40}")) {
            throw new IllegalArgumentException(
                    "La dirección de wallet no es válida"
            );
        }
    }

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
}
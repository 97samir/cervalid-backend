package com.cervalid.platform.blockchain.service;

import com.cervalid.platform.blockchain.contract.DocumentRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlockchainContractFactory {

    private final Web3j web3j;
    private final Credentials credentials;

    @Value("${blockchain.contract-address}")
    private String documentRegistryAddress;

    public DocumentRegistry loadDocumentRegistry() {
        if (documentRegistryAddress == null
                || documentRegistryAddress.isBlank()) {
            throw new IllegalStateException(
                    "La dirección de DocumentRegistry no está configurada"
            );
        }

        log.debug("Cargando DocumentRegistry en {} con wallet {}",
                documentRegistryAddress,
                credentials.getAddress());

        return DocumentRegistry.load(
                documentRegistryAddress,
                web3j,
                credentials,
                new DefaultGasProvider()
        );
    }
}
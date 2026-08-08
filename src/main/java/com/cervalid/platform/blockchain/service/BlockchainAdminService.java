package com.cervalid.platform.blockchain.service;

import com.cervalid.platform.blockchain.contract.CertificateRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainAdminService {

    private final Web3j web3j;
    private final Credentials credentials;

    @Value("${blockchain.contract-address}")
    private String contractAddress;

    private CertificateRegistry loadContract() {

        return CertificateRegistry.load(
                contractAddress,
                web3j,
                credentials,
                new DefaultGasProvider()
        );
    }

    public String registerInstitution(String walletAddress) {

        try {
            CertificateRegistry contract = loadContract();
            log.info("REGISTERING INSTITUTION: {}", walletAddress);
            var receipt = contract
                    .addInstitution(walletAddress)
                    .send();
            // validar estado blockchain
            if (!receipt.isStatusOK()) {
                throw new RuntimeException("Blockchain transaction failed");
            }

            String txHash = receipt.getTransactionHash();

            log.info("Institution registered on blockchain. TX: {}",
                    receipt.getTransactionHash()
            );

            return  txHash;

        } catch (Exception e) {
            throw new RuntimeException("Error registering institution on blockchain", e);
        }
    }

    public boolean isInstitution(String walletAddress) {
        try {
            log.info("CHECKING WALLET: {}", walletAddress);
            CertificateRegistry contract = loadContract();
            log.info("CONTRACT: {}", contract.getContractAddress());
            return contract.isInstitution(walletAddress).send();

        } catch (Exception e) {
            throw new RuntimeException("Error checking institution role", e);
        }
    }
}
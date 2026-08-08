package com.cervalid.platform.blockchain.service;

import com.cervalid.platform.blockchain.contract.CertificateRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class BlockchainContractFactory {

    private final Web3j web3j;
    private final Credentials credentials;

    @Value("${blockchain.contract-address}")
    private String contractAddress;

    public CertificateRegistry loadContract() {

        System.out.println("===== BLOCKCHAIN LOAD =====");
        System.out.println("Contract Address: " + contractAddress);
        System.out.println("Web3j: " + web3j);
        System.out.println("Credentials: " + credentials.getAddress());

        try {
            String code = web3j.ethGetCode(
                    contractAddress,
                    DefaultBlockParameterName.LATEST
            ).send().getCode();

            System.out.println("Contract code: " + code);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return CertificateRegistry.load(
                contractAddress,
                web3j,
                credentials,
                new DefaultGasProvider()
        );
    }
}
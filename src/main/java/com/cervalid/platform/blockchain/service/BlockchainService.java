package com.cervalid.platform.blockchain.service;

import com.cervalid.platform.blockchain.contract.CertificateRegistry;
import com.cervalid.platform.blockchain.dto.BlockchainAnchorResult;
import com.cervalid.platform.blockchain.mapper.BlockchainMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlockchainService {

    private final BlockchainContractFactory blockchainContractFactory;
    private final BlockchainMapper blockchainMapper;

    /*
    @Value("${blockchain.contract-address}")
    private String contractAddress;
    */
    @Value("${blockchain.network}")
    private String network;

    // REGISTER
    public BlockchainAnchorResult registerCertificate(
            String hash, String metadataURI) {

        try {

            CertificateRegistry contract =
                    blockchainContractFactory.loadContract();

            System.out.println("Hash enviado: " + hash);

            TransactionReceipt receipt = contract.registerCertificate(
                    hexStringToBytes32(hash),
                    metadataURI
            ).send();
            
            // Validar estado
            if (!receipt.isStatusOK()) {
                throw new RuntimeException("Transaction failed: " + receipt.getStatus());
            }

            // Leer eventos
            var events = contract.getCertificateRegisteredEvents(receipt);

            if (!events.isEmpty()) {
                var event = events.get(0);
                System.out.println("Certificado registrado:");
                System.out.println("Hash: " + event.hash);
                System.out.println("Institución: " + event.institution);
            }

            var raw = contract.getCertificate(hexStringToBytes32(hash)).send();
            System.out.println("RAW CONTRACT RESULT: " + raw.toString());

            return blockchainMapper.toResult(receipt, network);

        } catch (Exception e) {
            System.out.println("===== BLOCKCHAIN ERROR FULL STACK =====");
            e.printStackTrace();

            throw new RuntimeException(
                    "BLOCKCHAIN ERROR: " + e.getMessage(),
                    e
            );
        }
    }

    // VERIFY
    public boolean verifyCertificate(String hash) {
        try {
            CertificateRegistry contract =
                    blockchainContractFactory.loadContract();

            byte[] bytes32 = hexStringToBytes32(hash);

            System.out.println("===== BLOCKCHAIN DEBUG =====");
            System.out.println("Contract Address: " + System.getenv("BLOCKCHAIN_CONTRACT_ADDRESS"));
            System.out.println("Hash input: " + hash);
            System.out.println("Bytes32: " + java.util.Arrays.toString(bytes32));

            Boolean result = contract.verifyCertificate(bytes32).send();

            System.out.println("BLOCKCHAIN RESULT: " + result);

            return Boolean.TRUE.equals(result);

        } catch (Exception e) {
            System.out.println("BLOCKCHAIN ERROR FULL STACK:");
            e.printStackTrace();

            return false; // fallback seguro
        }
    }

    // REVOKE
    public BlockchainAnchorResult revokeCertificate(String hash) {
        try {
            CertificateRegistry contract =
                    blockchainContractFactory.loadContract();

            TransactionReceipt receipt =
                    contract.revokeCertificate(
                            hexStringToBytes32(hash))
                            .send();

            if (!receipt.isStatusOK()) {
                throw new RuntimeException("Transaction failed");
            }

            return blockchainMapper.toResult(receipt, network);

        } catch (Exception e) {
            throw new RuntimeException("Error revoking certificate", e);
        }
    }

    // REACTIVATE
    public BlockchainAnchorResult reactivateCertificate(String hash) {
        try {
            CertificateRegistry contract =
                    blockchainContractFactory.loadContract();

            TransactionReceipt receipt = contract
                    .reactivateCertificate(
                            hexStringToBytes32(hash))
                    .send();

            if (!receipt.isStatusOK()) {
                throw new RuntimeException("Transaction failed");
            }

            return blockchainMapper.toResult(receipt, network);

        } catch (Exception e) {
            throw new RuntimeException("Error reactivating certificate", e);
        }
    }

    // UTIL
    private byte[] hexStringToBytes32(String hex) {
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }

        byte[] bytes = new byte[32];
        byte[] input = org.web3j.utils.Numeric.hexStringToByteArray(hex);

        System.arraycopy(input, 0, bytes, 32 - input.length, input.length);

        return bytes;
    }

}

package com.cervalid.platform.blockchain.adapter;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BlockchainPayloadBuilder {

    public Map<String, Object> buildCertificateAnchor(
            String certificateHash,
            String verificationHash) {

        return Map.of(
                "certificateHash", certificateHash,
                "verificationHash", verificationHash
        );
    }
}

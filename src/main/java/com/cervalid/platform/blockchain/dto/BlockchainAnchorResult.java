package com.cervalid.platform.blockchain.dto;

import lombok.*;

@Data
public class BlockchainAnchorResult {

    private String txHash;
    private Long blockNumber;
    private String network;
}

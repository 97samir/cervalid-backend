package com.cervalid.platform.blockchain.mapper;

import com.cervalid.platform.blockchain.dto.BlockchainAnchorResult;
import lombok.RequiredArgsConstructor;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlockchainMapper {

    //private final String network = "POLYGON";

    public BlockchainAnchorResult toResult(
            TransactionReceipt receipt,
            String network) {

        BlockchainAnchorResult result = new BlockchainAnchorResult();

        result.setTxHash(receipt.getTransactionHash());
        result.setNetwork(network);

        if (receipt.getBlockNumber() != null) {
            result.setBlockNumber(receipt.getBlockNumber().longValue());
        }

        return result;
    }
}
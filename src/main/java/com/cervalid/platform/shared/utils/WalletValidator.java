package com.cervalid.platform.shared.utils;

public class WalletValidator {

    public static boolean isValidEthereumAddress(String address) {

        return address != null
                && address.matches("^0x[a-fA-F0-9]{40}$");
    }
}

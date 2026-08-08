package com.cervalid.platform.util;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtSecretGenerator {

    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[64]; // 512 bits (ideal para HS512)
        secureRandom.nextBytes(key);

        String secret = Base64.getEncoder().encodeToString(key);
        System.out.println("JWT_SECRET=" + secret);
    }
}

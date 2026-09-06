package com.cervalid.platform.shared.hashing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
@RequiredArgsConstructor
public class HashService {

    private final ObjectMapper mapper;

    public String sha256(Object payload) {
        try {
            String json = mapper.writeValueAsString(payload);
            return hashString(json);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing payload", e);
        }
    }

    public String hashString(String data) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            data.getBytes(StandardCharsets.UTF_8)
                    );

            return normalizeHex(
                    Hex.toHexString(hash)
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error hashing string",
                    e
            );
        }
    }

    public String hashFile(byte[] fileBytes) {
        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(fileBytes);

            return normalizeHex(
                    Hex.toHexString(hash)
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error hashing file",
                    e
            );
        }
    }

    public String normalizeHash(String hash) {

        if (hash == null) {
            return null;
        }

        return normalizeHex(
                hash.trim().toLowerCase()
        );
    }

    public boolean isValidSha256(String hash) {

        return hash != null
                && hash.matches(
                "^0x[a-fA-F0-9]{64}$"
        );
    }

    private String normalizeHex(String hash) {

        return hash.startsWith("0x")
                ? hash
                : "0x" + hash;
    }
}
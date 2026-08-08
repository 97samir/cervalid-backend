package com.cervalid.platform.academic.verification.proof;

import com.cervalid.platform.shared.hashing.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class VerificationProofService {

    private final HashService hashService;

    public String generateProof(String certificateHash, String transcriptHash) {

        Map<String, Object> payload = Map.of(
                "certificateHash", certificateHash,
                "transcriptHash", transcriptHash
        );

        return hashService.sha256(payload);
    }
}

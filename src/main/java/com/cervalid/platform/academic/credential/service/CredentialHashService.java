package com.cervalid.platform.academic.credential.service;

import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.shared.hashing.CanonicalHashService;
import com.cervalid.platform.shared.hashing.HashPayloadBuilder;
import com.cervalid.platform.shared.hashing.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CredentialHashService {

    private final HashPayloadBuilder hashPayloadBuilder;
    private final CanonicalHashService canonicalHashService;
    private final HashService hashService;

    public String generateCredentialHash(
            Credential credential
    ) {

        if (credential.getIssuedAt() == null) {
            throw new IllegalStateException(
                    "La credential debe tener fecha de emisión para generar su hash"
            );
        }

        Map<String, Object> payload =
                hashPayloadBuilder.buildCredentialHashPayload(
                        credential.getStudentId(),
                        credential.getInstitutionId(),
                        credential.getCredentialNumber(),
                        credential.getType().name(),
                        credential.getTitle(),
                        credential.getAwardedAt().toString(),
                        credential.getIssuedAt().toString()
                );

        String canonicalJson =
                canonicalHashService.canonicalize(payload);

        return hashService.hashString(canonicalJson);
    }
}
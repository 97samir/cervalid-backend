package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.domain.CertificateVerificationUrlBuilder;
import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.snapshot.CertificateSnapshotBuilder;
import com.cervalid.platform.academic.verification.proof.VerificationProofService;
import com.cervalid.platform.shared.hashing.CanonicalHashService;
import com.cervalid.platform.shared.hashing.HashPayloadBuilder;
import com.cervalid.platform.shared.hashing.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CertificateHashService {

    private final HashService hashService;
    private final CanonicalHashService canonicalHashService;
    private final HashPayloadBuilder hashPayloadBuilder;
    private final VerificationProofService verificationProofService;
    private final CertificateSnapshotBuilder snapshotBuilder;
    private final CertificateVerificationUrlBuilder verificationUrlBuilder;

    public void generateHashes(
            Certificate certificate) {

        Map<String, Object> payload =
                hashPayloadBuilder.buildCertificateHashPayload(
                        certificate.getStudentId(),
                        certificate.getInstitutionId(),
                        certificate.getCertificateNumber(),
                        certificate.getTitle(),
                        certificate.getAwardedAt().toString(),
                        certificate.getTranscriptHash(),
                        certificate.getIssuedAt().toString()
                );

        String canonicalJson = canonicalHashService.canonicalize(
                        payload);

        String certificateHash = hashService.hashString(
                        canonicalJson);

        String verificationHash =
                verificationProofService.generateProof(
                        certificateHash,
                        certificate.getTranscriptHash());

        certificate.setCertificateHash(certificateHash);
        certificate.setVerificationHash(verificationHash);

        certificate.setSnapshotJson(snapshotBuilder.build(
                        certificate));

        certificate.setVerificationUrl(
                verificationUrlBuilder.build(
                        certificate.getPublicId()));
    }

}
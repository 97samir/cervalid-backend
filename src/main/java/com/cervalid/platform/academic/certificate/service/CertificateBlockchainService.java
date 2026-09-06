package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.verification.proof.VerificationProofService;
import com.cervalid.platform.blockchain.adapter.BlockchainAnchorService;
import com.cervalid.platform.blockchain.adapter.BlockchainCertificatePayload;
import com.cervalid.platform.blockchain.dto.BlockchainAnchorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateBlockchainService {

    private final BlockchainAnchorService blockchainAnchorService;
    private final VerificationProofService verificationProofService;

    public void anchor(
            Certificate certificate) {

        try {
            /*
            String verificationHash = verificationProofService
                    .generateProof(
                            certificate.getCertificateHash(),
                            certificate.getTranscriptHash()
                    );
            */

            BlockchainCertificatePayload payload =
                    new BlockchainCertificatePayload();

            payload.setCertificatePublicId(certificate.getPublicId());
            payload.setCertificateNumber(certificate.getCertificateNumber());
            payload.setCertificateHash(certificate.getCertificateHash());
            payload.setTranscriptHash(certificate.getTranscriptHash());
            //payload.setVerificationHash(verificationHash);
            payload.setVerificationHash(certificate.getVerificationHash());
            payload.setStudentId(certificate.getStudentId());
            payload.setInstitutionId(certificate.getInstitutionId());
            payload.setTitle(certificate.getTitle());
            payload.setAwardedAt(certificate.getAwardedAt());
            payload.setDocumentHash(certificate.getDocumentHash());
            payload.setIssuedAt(certificate.getIssuedAt());
            payload.setNetwork("POLYGON");

            BlockchainAnchorResult result = blockchainAnchorService
                    .anchorCertificate(payload);

            certificate.setBlockchainTxHash(result.getTxHash());
            certificate.setBlockNumber(result.getBlockNumber());
            certificate.setNetwork(result.getNetwork());
            certificate.setAnchoredAt(LocalDateTime.now());

        } catch (Exception e) {

            log.error(
                    "Blockchain anchoring failed for certificate {}",
                    certificate.getPublicId(),
                    e
            );
        }
    }
}
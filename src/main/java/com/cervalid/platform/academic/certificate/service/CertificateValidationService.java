package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.enums.CertificateStatus;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.enums.TranscriptStatus;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateValidationService {

    private final CertificateRepository certificateRepository;
    private final SecurityContextService securityContextService;

    public void validateTranscriptForIssuance(
            Transcript transcript) {

        validateTranscriptExists(transcript);
        validateTranscriptIsFinalized(transcript);
        validateTranscriptHasHash(transcript);
        validateTranscriptNotAlreadyIssued(transcript);
    }

    public Certificate getCertificate(
            UUID publicId) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return certificateRepository
                .findByPublicIdAndInstitutionId(
                        publicId,
                        institutionId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Certificate not found"));
    }

    public void validateCanRevoke(
            Certificate certificate) {

        if (certificate.getStatus()
                == CertificateStatus.REVOKED) {

            throw new RuntimeException(
                    "Certificate already revoked");
        }
    }

    private void validateTranscriptExists(
            Transcript transcript) {

        if (transcript == null) {

            throw new RuntimeException(
                    "Transcript not found");
        }
    }

    private void validateTranscriptIsFinalized(
            Transcript transcript) {

        if (transcript.getStatus()
                != TranscriptStatus.FINALIZED) {

            throw new RuntimeException(
                    "Transcript must be FINALIZED");
        }
    }

    private void validateTranscriptHasHash(
            Transcript transcript) {

        if (transcript.getTranscriptHash() == null) {

            throw new RuntimeException(
                    "Transcript hash not generated");
        }
    }

    private void validateTranscriptNotAlreadyIssued(
            Transcript transcript) {

        if (certificateRepository
                .existsByTranscriptId(
                        transcript.getId())) {

            throw new RuntimeException(
                    "Certificate already issued");
        }
    }
}
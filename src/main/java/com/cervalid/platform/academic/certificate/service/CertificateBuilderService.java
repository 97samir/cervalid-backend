package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.enums.CertificateStatus;
import com.cervalid.platform.academic.certificate.enums.CertificateType;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateBuilderService {

    private final CertificateNumberGenerator certificateNumberGenerator;

    public Certificate build(
            Transcript transcript,
            CertificateType type) {

        return Certificate.builder()

                .publicId(UUID.randomUUID())
                .certificateNumber(certificateNumberGenerator.generate())
                .institutionId(transcript.getInstitutionId())
                .studentId(transcript.getStudentId())
                .transcriptId(transcript.getId())
                .transcriptHash(transcript.getTranscriptHash())
                .type(type)
                .status(CertificateStatus.ISSUED)
                .issuedAt(LocalDateTime.now())
                .build();
    }

}
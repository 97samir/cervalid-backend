package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.academic.timeline.domain.TimelineMetadataBuilder;
import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.cervalid.platform.academic.timeline.service.TimelineEventService;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CertificateTimelineService {

    private final TimelineEventService timelineEventService;
    private final TimelineMetadataBuilder metadataBuilder;
    private final AcademicProfileRepository academicProfileRepository;
    private final TranscriptRepository transcriptRepository;

    public void issued(Certificate certificate) {

        AcademicProfile profile =
                academicProfileRepository
                        .findByStudentIdAndInstitutionId(
                                certificate.getStudentId(),
                                certificate.getInstitutionId()
                        )
                        .orElse(null);

        Map<String, Object> metadataValues =
                new java.util.HashMap<>();

        metadataValues.put("certificateNumber",
                certificate.getCertificateNumber());

        metadataValues.put("type",
                certificate.getType().name());

        metadataValues.put("title",
                certificate.getTitle());

        metadataValues.put("awardedAt",
                certificate.getAwardedAt() != null
                        ? certificate.getAwardedAt().toString()
                        : null);

        if (profile != null) {

            metadataValues.put(
                    "program",
                    profile.getProgram() != null
                            ? profile.getProgram().name()
                            : null
            );

            metadataValues.put(
                    "faculty",
                    profile.getFaculty() != null
                            ? profile.getFaculty().name()
                            : null
            );
        }

        // dto respues al timeline meytadataJson
        JsonNode metadata =
                metadataBuilder.build(metadataValues);

        publish(
                certificate,
                TimelineEventType.CERTIFICATE_ISSUED,
                TimelineEventSource.SYSTEM,
                "Certificate issued",
                "Academic certificate issued",
                metadata
        );
    }

    public void revoked(Certificate certificate) {

        Map<String, Object> metadataValues =
                new HashMap<>();

        metadataValues.put("certificateNumber",
                certificate.getCertificateNumber());

        metadataValues.put("certificateType",
                certificate.getType().name());

        if (certificate.getRevokedAt() != null) {
            metadataValues.put(
                    "revokedAt",
                    certificate.getRevokedAt().toString()
            );
        }

        JsonNode metadata =
                metadataBuilder.build(metadataValues);

        publish(
                certificate,
                TimelineEventType.CERTIFICATE_REVOKED,
                TimelineEventSource.INSTITUTION_ADMIN,
                "Certificate revoked",
                "Certificate revoked by institution",
                metadata
        );
    }

    public void credentialUpdated(
            Certificate certificate) {

        Map<String, Object> metadataValues =
                new java.util.HashMap<>();

        metadataValues.put("certificateNumber",
                certificate.getCertificateNumber());

        metadataValues.put("type",
                certificate.getType().name());

        metadataValues.put("title",
                certificate.getTitle());

        metadataValues.put("awardedAt",
                certificate.getAwardedAt().toString());

        JsonNode metadata =
                metadataBuilder.build(
                        metadataValues
                );

        publish(
                certificate,
                TimelineEventType.CREDENTIAL_UPDATED,
                TimelineEventSource.INSTITUTION_ADMIN,
                "Credential updated",
                "Certificate credential information updated",
                metadata
        );
    }

    private void publish(
            Certificate certificate,
            TimelineEventType eventType,
            TimelineEventSource source,
            String title,
            String description,
            JsonNode metadata) {

        String academicPeriod = resolveAcademicPeriod(certificate);

        timelineEventService.createEvent(
                certificate.getInstitutionId(),
                certificate.getStudentId(),
                eventType,
                source,
                title,
                description,
                certificate.getPublicId(),
                TimelineReferenceType.CERTIFICATE,
                academicPeriod,
                metadata
        );
    }

    private String resolveAcademicPeriod(
            Certificate certificate) {

        if (certificate
                .getTranscriptId() == null) {
            return null;
        } return transcriptRepository
                .findById(
                        certificate.getTranscriptId()
                )
                .map(Transcript::getAcademicPeriod)
                .orElse(null);
    }
}
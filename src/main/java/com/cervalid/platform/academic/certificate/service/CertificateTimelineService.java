package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.timeline.domain.TimelineMetadataBuilder;
import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.cervalid.platform.academic.timeline.service.TimelineEventService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CertificateTimelineService {

    private final TimelineEventService timelineEventService;
    private final TimelineMetadataBuilder metadataBuilder;

    public void issued(
            Certificate certificate) {

        JsonNode metadata =
                metadataBuilder.build(
                        Map.of(
                                "certificateNumber",
                                certificate.getCertificateNumber(),

                                "type",
                                certificate.getType().name()
                        )
                );

        publish(
                certificate,
                TimelineEventType.CERTIFICATE_ISSUED,
                "Certificate issued",
                "Academic certificate issued",
                metadata
        );
    }

    public void revoked(
            Certificate certificate) {

        JsonNode metadata = metadataBuilder.build(
                Map.of(
                        "certificateNumber", certificate.getCertificateNumber(),
                        "certificateType", certificate.getType().name(),
                        "revokedAt", certificate.getRevokedAt().toString()
                        )
                );

        publish(
                certificate,
                TimelineEventType.CERTIFICATE_REVOKED,
                "Certificate revoked",
                "Certificate revoked by institution",
                metadata
        );
    }

    private void publish(
            Certificate certificate,
            TimelineEventType eventType,
            String title,
            String description,
            JsonNode metadata) {

        timelineEventService.createEvent(
                certificate.getInstitutionId(),
                certificate.getStudentId(),
                eventType,
                TimelineEventSource.SYSTEM,
                title,
                description,
                certificate.getPublicId(),
                TimelineReferenceType.CERTIFICATE,
                metadata
        );
    }
}
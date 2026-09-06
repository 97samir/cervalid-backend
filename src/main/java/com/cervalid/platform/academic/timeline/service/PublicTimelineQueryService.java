package com.cervalid.platform.academic.timeline.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.timeline.domain.PublicTimelinePolicy;
import com.cervalid.platform.academic.timeline.dto.view.PublicTimelineEventView;
import com.cervalid.platform.academic.timeline.entity.TimelineEvent;
import com.cervalid.platform.academic.timeline.repository.TimelineEventRepository;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublicTimelineQueryService {

    private final TimelineEventRepository repository;
    private final PublicTimelinePolicy publicTimelinePolicy;
    private final CertificateRepository certificateRepository;

    @Transactional(readOnly = true)
    public List<PublicTimelineEventView> getPublicTimelineByCertificate(
            UUID certificatePublicId) {

        Certificate certificate =
                certificateRepository
                        .findByPublicId(certificatePublicId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Certificate not found"
                                )
                        );

        return getPublicTimeline(
                certificate.getInstitutionId(),
                certificate.getStudentId()
        );
    }

    @Transactional(readOnly = true)
    public List<PublicTimelineEventView> getPublicTimeline(
            Long institutionId,
            Long studentId) {

        List<TimelineEvent> events =
                repository
                        .findByInstitutionIdAndStudentIdAndDeletedFalseOrderByEventDateDesc(
                                institutionId,
                                studentId
                        );

        return events.stream()
                // Solo eventos permitidos públicamente
                .filter(event ->
                        publicTimelinePolicy.isPublic(
                                event.getType()))
                .map(this::toView)
                .toList();
    }

    private PublicTimelineEventView toView(
            TimelineEvent event) {

        return PublicTimelineEventView.builder()
                .publicId(event.getPublicId())
                .type(event.getType())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .referenceType(event.getReferenceType())
                .academicPeriod(event.getAcademicPeriod())
                .metadataJson(event.getMetadataJson())
                .build();
    }
}
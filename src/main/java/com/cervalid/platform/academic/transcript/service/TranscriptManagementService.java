package com.cervalid.platform.academic.transcript.service;

import com.cervalid.platform.academic.competency.engine.CompetencyGenerationEngine;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.service.StudentQueryService;
import com.cervalid.platform.academic.timeline.domain.TimelineMetadataBuilder;
import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.cervalid.platform.academic.timeline.service.TimelineEventService;
import com.cervalid.platform.academic.transcript.domain.TranscriptHashService;
import com.cervalid.platform.academic.transcript.domain.TranscriptSnapshotDTO;
import com.cervalid.platform.academic.transcript.domain.TranscriptSnapshotService;
import com.cervalid.platform.academic.transcript.domain.TranscriptStatusManager;
import com.cervalid.platform.academic.transcript.dto.request.CreateTranscriptRequest;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import com.cervalid.platform.academic.transcript.enums.TranscriptStatus;
import com.cervalid.platform.academic.transcript.mapper.TranscriptSnapshotBuilder;
import com.cervalid.platform.academic.transcript.repository.TranscriptItemRepository;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import com.cervalid.platform.security.context.SecurityContextService;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TranscriptManagementService {

    private final TranscriptRepository transcriptRepository;
    private final TranscriptItemRepository itemRepository;

    private final StudentQueryService studentQueryService;
    private final TranscriptValidationService validationService;
    private final SecurityContextService securityContextService;
    private final PublicIdGenerator publicIdGenerator;

    private final TranscriptHashService transcriptHashService;

    private final TranscriptSnapshotBuilder snapshotBuilder;
    private final TranscriptSnapshotService transcriptSnapshotService;

    private final TimelineEventService timelineEventService;
    private final TimelineMetadataBuilder timelineMetadataBuilder;

    private final CompetencyGenerationEngine competencyGenerationEngine;

    private final TranscriptQueryService queryService;
    private final TranscriptStatusManager statusManager;

    // CREAR TRANSCRIPT
    public Transcript create(
            UUID studentPublicId,
            CreateTranscriptRequest request) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Student student =
                studentQueryService.getByPublicId(
                        studentPublicId
                );

        validationService.validate(
                student,
                request
        );

        Transcript transcript =
                Transcript.builder()
                        .publicId(publicIdGenerator.generate())
                        .studentId(student.getId())
                        .institutionId(institutionId)
                        .academicPeriodType(request.getAcademicPeriodType())
                        .academicPeriod(request.getAcademicPeriod())
                        .status(TranscriptStatus.DRAFT)
                        .build();

        Transcript saved =
                transcriptRepository.save(transcript);

        List<TranscriptItem> items =
                request.getItems()
                        .stream()
                        .map(i ->
                                TranscriptItem.builder()
                                        .publicId(publicIdGenerator.generate())
                                        .transcriptId(saved.getId())
                                        .courseCode(i.getCourseCode())
                                        .courseName(i.getCourseName())
                                        .credits(i.getCredits())
                                        .grade(i.getGrade())
                                        .build()
                        )
                        .toList();

        itemRepository.saveAll(items);

        System.out.println("CREATED TRANSCRIPT PERIOD: " + transcript.getAcademicPeriod());
        System.out.println("TRANSCRIPT ID: " + transcript.getPublicId());
        // TIMELINE
        JsonNode metadata = timelineMetadataBuilder.build(
                Map.of(
                        "period", saved.getAcademicPeriod(),
                        "courses", items.size()
                        )
                );

        timelineEventService.createEvent(
                institutionId,
                student.getId(),
                TimelineEventType.TRANSCRIPT_CREATED,
                TimelineEventSource.INSTITUTION_ADMIN,
                "Transcript generated",
                "Academic transcript created",
                saved.getPublicId(),
                TimelineReferenceType.TRANSCRIPT,
                saved.getAcademicPeriod(),
                metadata
        );

        return saved;
    }

    // FINALIZAR TRANSCRIPT
    public void finalizeTranscript(
            UUID transcriptPublicId) {

        Transcript transcript =
                queryService.getByPublicId(
                        transcriptPublicId
                );

        Student student =
                studentQueryService.getById(
                        transcript.getStudentId()
                );

        List<TranscriptItem> items =
                itemRepository.findByTranscriptId(
                        transcript.getId()
                );

        // GENERACIÓN ÚNICA DEL HASH
        String hash =
                transcriptHashService.generateHash(
                        transcript,
                        items
                );

        // TRANSICIÓN DE ESTADO
        statusManager.finalizeTranscript(
                transcript,
                hash
        );

        // GENERACIÓN DE COMPETENCIAS
        competencyGenerationEngine.generateFromTranscript(
                transcript
        );

        // SNAPSHOT
        TranscriptSnapshotDTO snapshot =
                snapshotBuilder.build(
                        transcript,
                        student,
                        items
                );

        transcriptSnapshotService.saveSnapshot(
                transcript.getId(),
                snapshot,
                transcript.getTranscriptHash()
        );

        transcriptRepository.save(transcript);

        System.out.println("FINALIZED TRANSCRIPT PERIOD: " + transcript.getAcademicPeriod());
        System.out.println("TRANSCRIPT ID: " + transcript.getPublicId());

        // TIMELINE
        JsonNode metadata =
                timelineMetadataBuilder.build(
                        Map.of(
                                "period",
                                transcript.getAcademicPeriod(),

                                "courses",
                                items.size()
                        )
                );

        timelineEventService.createEvent(
                transcript.getInstitutionId(),
                transcript.getStudentId(),
                TimelineEventType.TRANSCRIPT_FINALIZED,
                TimelineEventSource.INSTITUTION_ADMIN,
                "Transcript finalized",
                transcript.getAcademicPeriod(),
                transcript.getPublicId(),
                TimelineReferenceType.TRANSCRIPT,
                transcript.getAcademicPeriod(),
                metadata
        );
    }

    // EMITIR TRANSCRIPT
    public void issueTranscript(
            UUID transcriptPublicId) {

        Transcript transcript =
                queryService.getByPublicId(
                        transcriptPublicId
                );

        validationService.validateCanIssue(transcript);
        statusManager.issueTranscript(transcript);
        transcriptRepository.save(transcript);

        // TIMELINE
        JsonNode metadata =
                timelineMetadataBuilder.build(
                        Map.of(
                                "period",
                                transcript.getAcademicPeriod(),

                                "issuedAt",
                                transcript.getIssuedAt()
                                        .toString()
                        )
                );

        timelineEventService.createEvent(
                transcript.getInstitutionId(),
                transcript.getStudentId(),
                TimelineEventType.TRANSCRIPT_ISSUED,
                TimelineEventSource.INSTITUTION_ADMIN,
                "Transcript issued",
                "Official academic transcript issued",
                transcript.getPublicId(),
                TimelineReferenceType.TRANSCRIPT,
                transcript.getAcademicPeriod(),
                metadata
        );
    }
}
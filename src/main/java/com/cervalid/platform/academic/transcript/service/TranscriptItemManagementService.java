package com.cervalid.platform.academic.transcript.service;

import com.cervalid.platform.academic.timeline.domain.TimelineMetadataBuilder;
import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.cervalid.platform.academic.timeline.service.TimelineEventService;
import com.cervalid.platform.academic.transcript.domain.TranscriptStatusManager;
import com.cervalid.platform.academic.transcript.dto.item.TranscriptItemDTO;
import com.cervalid.platform.academic.transcript.dto.request.TranscriptItemRequest;
import com.cervalid.platform.academic.transcript.dto.request.UpdateTranscriptItemRequest;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import com.cervalid.platform.academic.transcript.mapper.TranscriptItemMapper;
import com.cervalid.platform.academic.transcript.repository.TranscriptItemRepository;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TranscriptItemManagementService {

    private final TranscriptService transcriptService;
    private final TranscriptItemRepository itemRepository;
    private final TranscriptValidationService validationService;
    private final TranscriptStatusManager transcriptStatusManager;
    private final TimelineEventService timelineEventService;
    private final TimelineMetadataBuilder metadataBuilder;
    private final TranscriptItemMapper mapper;
    private final PublicIdGenerator publicIdGenerator;

    public TranscriptItemDTO addItem(
            UUID transcriptPublicId,
            TranscriptItemRequest request) {

        Transcript transcript =
                transcriptService.getEntityByPublicId(
                        transcriptPublicId);

        validationService.validateCanModify(transcript);

        if (itemRepository.existsByTranscriptIdAndCourseCode(
                transcript.getId(),
                request.getCourseCode())) {

            throw new RuntimeException(
                    "Course already exists in transcript");
        }

        TranscriptItem item =
                TranscriptItem.builder()
                        .publicId(publicIdGenerator.generate())
                        .transcriptId(transcript.getId())
                        .courseCode(request.getCourseCode())
                        .courseName(request.getCourseName())
                        .credits(request.getCredits())
                        .grade(request.getGrade())
                        .build();

        TranscriptItem saved =
                itemRepository.save(item);

        publishTimeline(
                transcript,
                TimelineEventType.TRANSCRIPT_ITEM_ADDED,
                "Course added",
                saved.getCourseCode(),
                saved.getCourseName());

        transcriptStatusManager.invalidateTranscript(transcript);

        return mapper.toResponse(saved);
    }

    public TranscriptItemDTO updateItem(
            UUID transcriptPublicId,
            UUID itemPublicId,
            UpdateTranscriptItemRequest request) {

        Transcript transcript =
                transcriptService.getEntityByPublicId(
                        transcriptPublicId);

        validationService.validateCanModify(transcript);

        TranscriptItem item =
                itemRepository
                        .findByPublicIdAndTranscriptId(
                                itemPublicId,
                                transcript.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transcript item not found"));
/*
        if (itemRepository.existsByTranscriptIdAndCourseCodeAndIdNot(
                transcript.getId(),
                request.getCourseCode(),
                item.getId())) {

            throw new RuntimeException(
                    "Course already exists in transcript");
        }

 */
        //item.setCourseCode(request.getCourseCode());
        item.setCourseName(request.getCourseName());
        item.setCredits(request.getCredits());
        item.setGrade(request.getGrade());

        TranscriptItem saved =
                itemRepository.save(item);

        publishTimeline(
                transcript,
                TimelineEventType.TRANSCRIPT_ITEM_UPDATED,
                "Course updated",
                saved.getCourseCode(),
                saved.getCourseName());

        transcriptStatusManager.invalidateTranscript(transcript);

        return mapper.toResponse(saved);
    }

    public void deleteItem(
            UUID transcriptPublicId,
            UUID itemPublicId) {

        Transcript transcript =
                transcriptService.getEntityByPublicId(
                        transcriptPublicId);

        validationService.validateCanModify(transcript);

        TranscriptItem item =
                itemRepository
                        .findByPublicIdAndTranscriptId(
                                itemPublicId,
                                transcript.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transcript item not found"));

        publishTimeline(
                transcript,
                TimelineEventType.TRANSCRIPT_ITEM_REMOVED,
                "Course removed",
                item.getCourseCode(),
                item.getCourseName());

        itemRepository.delete(item);

        transcriptStatusManager.invalidateTranscript(transcript);
    }

    private void publishTimeline(
            Transcript transcript,
            TimelineEventType type,
            String title,
            String courseCode,
            String courseName) {

        JsonNode metadata =
                metadataBuilder.build(
                        Map.of(
                                "courseCode", courseCode,
                                "courseName", courseName
                        )
                );

        timelineEventService.createEvent(
                transcript.getInstitutionId(),
                transcript.getStudentId(),
                type,
                TimelineEventSource.INSTITUTION_ADMIN,
                title,
                courseCode,
                transcript.getPublicId(),
                TimelineReferenceType.TRANSCRIPT,
                transcript.getAcademicPeriod(),
                metadata
        );
    }

}
package com.cervalid.platform.academic.transcript.service;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.transcript.dto.request.CreateTranscriptRequest;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import com.cervalid.platform.academic.transcript.enums.TranscriptStatus;
import com.cervalid.platform.academic.transcript.repository.TranscriptItemRepository;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import com.cervalid.platform.academic.transcript.validation.AcademicPeriodValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranscriptValidationService {

    private final TranscriptRepository transcriptRepository;
    private final TranscriptItemRepository itemRepository;
    private final AcademicPeriodValidator academicPeriodValidator;

    public void validate(
            Student student,
            CreateTranscriptRequest request) {

        academicPeriodValidator.validate(
                request.getAcademicPeriodType(),
                request.getAcademicPeriod());

        if (request.getItems() == null ||
                request.getItems().isEmpty()) {

            throw new RuntimeException(
                    "Transcript must contain courses");
        }

        boolean exists =
                transcriptRepository
                        .existsByStudentIdAndAcademicPeriodTypeAndAcademicPeriod(
                                student.getId(),
                                request.getAcademicPeriodType(),
                                request.getAcademicPeriod());

        if (exists) {
            throw new RuntimeException(
                    "Transcript already exists for academic period");
        }
    }

    public void validateCanModify(
            Transcript transcript) {

        if (transcript == null) {
            throw new RuntimeException(
                    "Transcript not found");
        }

        if (transcript.getStatus()
                != TranscriptStatus.DRAFT) {

            throw new RuntimeException(
                    "Transcript cannot be modified unless it is DRAFT");
        }
    }

    public void validateCanIssue(
            Transcript transcript) {

        if (transcript.getStatus()
                != TranscriptStatus.FINALIZED) {

            throw new RuntimeException(
                    "Transcript must be FINALIZED before issuing");
        }

        List<TranscriptItem> items =
                itemRepository.findByTranscriptId(
                        transcript.getId());

        if (items.isEmpty()) {
            throw new RuntimeException(
                    "Transcript contains no academic records");
        }
    }

    public void validateCanFinalize(
            Transcript transcript) {

        if (transcript == null) {
            throw new RuntimeException(
                    "Transcript not found");
        }

        if (transcript.getStatus()
                != TranscriptStatus.DRAFT) {

            throw new RuntimeException(
                    "Only DRAFT transcript can be finalized");
        }

        List<TranscriptItem> items =
                itemRepository.findByTranscriptId(
                        transcript.getId());

        if (items.isEmpty()) {
            throw new RuntimeException(
                    "Transcript must contain academic records");
        }
    }
}
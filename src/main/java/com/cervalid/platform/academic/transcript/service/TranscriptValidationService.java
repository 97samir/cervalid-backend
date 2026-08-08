package com.cervalid.platform.academic.transcript.service;

import com.cervalid.platform.academic.shared.validation.InstitutionOwnershipValidator;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.academic.transcript.dto.request.CreateTranscriptRequest;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import com.cervalid.platform.academic.transcript.enums.TranscriptStatus;
import com.cervalid.platform.academic.transcript.repository.TranscriptItemRepository;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranscriptValidationService {

    private final StudentRepository studentRepository;
    private final TranscriptRepository transcriptRepository;
    private final TranscriptItemRepository itemRepository;
    private final InstitutionOwnershipValidator institutionOwnershipValidator;

    public Student validate(
            Student student,
            CreateTranscriptRequest request) {

        if (request.getItems() == null ||
                request.getItems().isEmpty()) {

            throw new RuntimeException(
                    "Transcript must contain courses");
        }

        request.getItems().forEach(item -> {

            if (item.getCredits() <= 0) {
                throw new RuntimeException(
                        "Credits must be greater than zero");
            }
        });

        boolean exists = transcriptRepository
                        .existsByStudentIdAndAcademicPeriod(
                                student.getId(),
                                request.getAcademicPeriod());

        if (exists) {
            throw new RuntimeException(
                    "Transcript already exists for period");
        }

        return student;
    }

    public void validateCanModify(Transcript transcript) {

        if (transcript == null) {
            throw new RuntimeException("Transcript not found");
        }

        if (transcript.getStatus() != TranscriptStatus.DRAFT) {
            throw new RuntimeException(
                    "Transcript cannot be modified unless it is DRAFT");
        }
    }

    public void validateCanIssue(
            Transcript transcript) {

        List<TranscriptItem> items =
                itemRepository.findByTranscriptId(
                        transcript.getId());

        if (items.isEmpty()) {

            throw new RuntimeException(
                    "Transcript contains no academic records");
        }
    }
}
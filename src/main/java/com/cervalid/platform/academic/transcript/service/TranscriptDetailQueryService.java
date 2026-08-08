package com.cervalid.platform.academic.transcript.service;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.service.StudentQueryService;
import com.cervalid.platform.academic.transcript.dto.item.TranscriptItemDTO;
import com.cervalid.platform.academic.transcript.dto.response.TranscriptDetailResponse;
import com.cervalid.platform.academic.transcript.dto.response.TranscriptResponse;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import com.cervalid.platform.academic.transcript.mapper.TranscriptItemMapper;
import com.cervalid.platform.academic.transcript.mapper.TranscriptMapper;
import com.cervalid.platform.academic.transcript.repository.TranscriptItemRepository;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranscriptDetailQueryService {

    private final TranscriptQueryService queryService;
    private final TranscriptRepository repository;
    private final TranscriptItemRepository itemRepository;
    private final TranscriptMapper mapper;
    private final TranscriptItemMapper itemMapper;
    private final StudentQueryService studentQueryService;
    private final SecurityContextService securityContextService;
    private final TranscriptQueryService transcriptQueryService;

    public TranscriptDetailResponse getDetail(
            UUID transcriptPublicId) {

        Transcript transcript =
                queryService.getByPublicId(
                        transcriptPublicId);

        List<TranscriptItem> items =
                itemRepository.findByTranscriptId(
                        transcript.getId());

        return buildDetail(transcript, items);
    }

    public List<TranscriptResponse> getByStudent(
            UUID studentPublicId) {

        Student student =
                studentQueryService.getByPublicId(
                        studentPublicId);

        Long institutionId =
                securityContextService.getInstitutionId();

        return repository
                .findByStudentIdAndInstitutionId(
                        student.getId(),
                        institutionId)
                .stream()
                .map(transcriptQueryService::toResponse)
                .toList();
    }

    private TranscriptDetailResponse buildDetail(
            Transcript transcript,
            List<TranscriptItem> items) {

        int creditsEarned =
                items.stream()
                        .mapToInt(
                                TranscriptItem::getCredits)
                        .sum();

        BigDecimal weightedSum =
                items.stream()
                        .map(item ->
                                item.getGrade().multiply(
                                        BigDecimal.valueOf(
                                                item.getCredits())))
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        BigDecimal gpa =
                creditsEarned == 0
                        ? BigDecimal.ZERO
                        : weightedSum.divide(
                        BigDecimal.valueOf(
                                creditsEarned),
                        2,
                        RoundingMode.HALF_UP);

        List<TranscriptItemDTO> itemDtos =
                items.stream()
                        .map(itemMapper::toResponse)
                        .toList();

        return TranscriptDetailResponse.builder()
                .publicId(transcript.getPublicId())
                .academicPeriod(transcript.getAcademicPeriod())
                .status(transcript.getStatus().name())
                .hash(transcript.getTranscriptHash())
                .issuedAt(transcript.getIssuedAt())
                .gpa(gpa)
                .creditsEarned(creditsEarned)
                .coursesCount(items.size())
                .items(itemDtos)
                .build();
    }

}
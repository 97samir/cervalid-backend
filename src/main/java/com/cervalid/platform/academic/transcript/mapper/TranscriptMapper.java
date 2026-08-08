package com.cervalid.platform.academic.transcript.mapper;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.transcript.domain.TranscriptSummaryCalculator;
import com.cervalid.platform.academic.transcript.dto.response.TranscriptResponse;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import com.cervalid.platform.academic.transcript.repository.TranscriptItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TranscriptMapper {

    private final TranscriptItemRepository itemRepository;
    private final TranscriptSummaryCalculator summaryCalculator;

    public TranscriptResponse toResponse(
            Transcript transcript,
            UUID certificatePublicId) {

        List<TranscriptItem> items =
                itemRepository.findByTranscriptId(
                        transcript.getId());

        var summary = summaryCalculator.calculate(items);

        return TranscriptResponse.builder()

                .publicId(transcript.getPublicId())
                .academicPeriod(transcript.getAcademicPeriod())
                .status(transcript.getStatus().name())
                .hash(transcript.getTranscriptHash())

                .certificatePublicId(certificatePublicId)

                .coursesCount(summary.getCoursesCount())
                .creditsEarned(summary.getCreditsEarned())
                .gpa(summary.getGpa())

                .build();
    }

}

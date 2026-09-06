package com.cervalid.platform.academic.transcript.domain;

import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import com.cervalid.platform.shared.hashing.CanonicalHashService;
import com.cervalid.platform.shared.hashing.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class TranscriptHashService {

    private final CanonicalHashService canonicalHashService;
    private final HashService hashService;

    public String generateHash(
            Transcript transcript,
            List<TranscriptItem> items) {

        List<Map<String, Object>> normalizedItems =
                items.stream()
                        .sorted(
                                Comparator.comparing(
                                        TranscriptItem::getCourseCode))
                        .map(item -> Map.<String, Object>of(
                                "courseCode", item.getCourseCode(),
                                "courseName", item.getCourseName(),
                                "credits", item.getCredits(),
                                "grade", item.getGrade()
                        ))
                        .toList();

        Map<String, Object> payload = Map.of(

                "transcriptPublicId", transcript.getPublicId(),
                "academicPeriodType", transcript.getAcademicPeriodType(),
                "academicPeriod", transcript.getAcademicPeriod(),
                "items", normalizedItems
                );

        String canonicalJson =
                canonicalHashService.canonicalize(payload);

        return hashService.hashString(canonicalJson);
    }
}
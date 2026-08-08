package com.cervalid.platform.academic.transcript.domain;

import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class TranscriptSummaryCalculator {

    public Summary calculate(List<TranscriptItem> items) {

        int courses = items.size();

        int credits = items.stream()
                .mapToInt(TranscriptItem::getCredits)
                .sum();

        BigDecimal weighted = items.stream()
                .map(i ->
                        i.getGrade().multiply(
                                BigDecimal.valueOf(
                                        i.getCredits())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal gpa =
                credits == 0
                        ? BigDecimal.ZERO
                        : weighted.divide(
                        BigDecimal.valueOf(credits),
                        2,
                        RoundingMode.HALF_UP);

        return Summary.builder()
                .coursesCount(courses)
                .creditsEarned(credits)
                .gpa(gpa)
                .build();
    }

    @Data
    @Builder
    public static class Summary {

        private Integer coursesCount;
        private Integer creditsEarned;
        private BigDecimal gpa;
    }

}
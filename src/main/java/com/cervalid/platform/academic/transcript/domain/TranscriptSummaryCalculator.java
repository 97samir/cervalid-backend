package com.cervalid.platform.academic.transcript.domain;

import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class TranscriptSummaryCalculator {

    private static final BigDecimal MIN_PASSING_GRADE =
            BigDecimal.valueOf(11);

    public Summary calculate(
            List<TranscriptItem> items) {

        int coursesCount =
                items.size();

        int creditsEarned =
                items.stream()
                        .filter(this::isPassed)
                        .mapToInt(TranscriptItem::getCredits)
                        .sum();

        int creditsFailed =
                items.stream()
                        .filter(item -> !isPassed(item))
                        .mapToInt(TranscriptItem::getCredits)
                        .sum();

        int totalCredits =
                creditsEarned + creditsFailed;

        BigDecimal weighted =
                items.stream()
                        .map(item ->
                                item.getGrade()
                                        .multiply(
                                                BigDecimal.valueOf(
                                                        item.getCredits())))
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        BigDecimal gpa =
                totalCredits == 0
                        ? BigDecimal.ZERO
                        : weighted.divide(
                        BigDecimal.valueOf(totalCredits),
                        2,
                        RoundingMode.HALF_UP);

        return Summary.builder()
                .coursesCount(coursesCount)
                .creditsEarned(creditsEarned)
                .creditsFailed(creditsFailed)
                .gpa(gpa)
                .build();
    }

    private boolean isPassed(
            TranscriptItem item) {

        return item.getGrade()
                .compareTo(MIN_PASSING_GRADE) >= 0;
    }

    @lombok.Data
    @lombok.Builder
    public static class Summary {

        private Integer coursesCount;
        private Integer creditsEarned;
        private Integer creditsFailed;
        private BigDecimal gpa;
    }
}
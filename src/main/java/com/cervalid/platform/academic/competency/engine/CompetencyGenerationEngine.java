package com.cervalid.platform.academic.competency.engine;

import com.cervalid.platform.academic.competency.entity.Competency;
import com.cervalid.platform.academic.competency.enums.CompetencyEvidenceType;
import com.cervalid.platform.academic.competency.enums.CompetencySource;
import com.cervalid.platform.academic.competency.enums.CompetencyStatus;
import com.cervalid.platform.academic.competency.repository.CompetencyRepository;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import com.cervalid.platform.academic.transcript.repository.TranscriptItemRepository;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetencyGenerationEngine {

    private final TranscriptItemRepository itemRepository;
    private final CompetencyRepository competencyRepository;
    private final TranscriptCompetencyRuleProvider ruleProvider;
    private final PublicIdGenerator publicIdGenerator;

    @Transactional
    public void generateFromTranscript(
            Transcript transcript) {

        List<TranscriptItem> items =
                itemRepository.findByTranscriptId(
                        transcript.getId());

        for (TranscriptItem item : items) {

            ruleProvider.findRule(
                    transcript.getInstitutionId(),
                            item.getCourseCode())
                    .filter(rule ->
                            shouldGenerateCompetency(
                                    transcript,
                                    item,
                                    rule))
                    .ifPresent(rule ->
                            createCompetency(
                                    transcript,
                                    rule));
        }
    }

    private boolean shouldGenerateCompetency(
            Transcript transcript,
            TranscriptItem item,
            CompetencyRule rule) {

        boolean approved =
                item.getGrade()
                        .compareTo(BigDecimal.valueOf(14)) >= 0;

        boolean exists =
                competencyRepository
                        .existsByStudentIdAndNameAndLevel(
                                transcript.getStudentId(),
                                rule.competencyName(),
                                rule.level());

        return approved && !exists;
    }

    private void createCompetency(
            Transcript transcript,
            CompetencyRule rule) {

        Competency competency =
                Competency.builder()
                        .publicId(publicIdGenerator.generate())
                        .institutionId(transcript.getInstitutionId())
                        .studentId(transcript.getStudentId())
                        //.studentPublicId(transcript.getPublicId())
                        .name(rule.competencyName())
                        .description(rule.description())
                        .level(rule.level())
                        .source(CompetencySource.TRANSCRIPT)
                        .evidenceType(CompetencyEvidenceType.TRANSCRIPT)
                        .evidenceReference(transcript.getPublicId())
                        .issuer("Cervalid Engine")
                        .status(CompetencyStatus.ACTIVE)
                        .build();

        competencyRepository.save(competency);
    }
}
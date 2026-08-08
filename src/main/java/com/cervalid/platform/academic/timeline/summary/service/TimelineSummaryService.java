package com.cervalid.platform.academic.timeline.summary.service;

import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.repository.TimelineEventRepository;
import com.cervalid.platform.academic.timeline.summary.dto.response.TimelineSummaryResponse;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimelineSummaryService {

    private final TimelineEventRepository repository;
    private final SecurityContextService securityContextService;

    public TimelineSummaryResponse getSummary() {

        Long institutionId =
                securityContextService.getInstitutionId();

        return TimelineSummaryResponse.builder()
                .totalEvents(
                        repository.countByInstitutionId(
                                institutionId))

                .studentCreated(
                        repository.countByInstitutionIdAndType(
                                institutionId,
                                TimelineEventType.STUDENT_REGISTERED))

                .profileCreated(
                        repository.countByInstitutionIdAndType(
                                institutionId,
                                TimelineEventType.PROFILE_CREATED))

                .transcriptCreated(
                        repository.countByInstitutionIdAndType(
                                institutionId,
                                TimelineEventType.TRANSCRIPT_CREATED))

                .certificateIssued(
                        repository.countByInstitutionIdAndType(
                                institutionId,
                                TimelineEventType.CERTIFICATE_ISSUED))

                .manualEvents(
                        repository.countByInstitutionIdAndType(
                                institutionId,
                                TimelineEventType.MANUAL_EVENT))

                .build();
    }
}
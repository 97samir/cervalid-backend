package com.cervalid.platform.academic.timeline.summary.service;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.repository.TimelineEventRepository;
import com.cervalid.platform.academic.timeline.summary.dto.response.TimelineSummaryResponse;
import com.cervalid.platform.academic.timeline.validation.TimelineOwnershipValidator;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineSummaryService {

    private final TimelineEventRepository repository;
    private final SecurityContextService securityContextService;
    private final TimelineOwnershipValidator ownershipValidator;

    public TimelineSummaryResponse getStudentSummary(
            UUID studentPublicId) {

        if (studentPublicId == null) {
            throw new IllegalArgumentException(
                    "studentPublicId is required"
            );
        }

        Long institutionId =
                securityContextService.getInstitutionId();

        if (institutionId == null) {
            throw new IllegalStateException(
                    "Institution context is required"
            );
        }

        Student student =
                ownershipValidator.validateStudentOwnership(
                        studentPublicId,
                        institutionId
                );

        Long studentId = student.getId();

        return TimelineSummaryResponse.builder()

                .totalEvents(
                        repository.countByInstitutionIdAndStudentIdAndDeletedFalse(
                                institutionId,
                                studentId
                        )
                )

                .studentCreated(
                        repository.countByInstitutionIdAndStudentIdAndTypeAndDeletedFalse(
                                institutionId,
                                studentId,
                                TimelineEventType.STUDENT_REGISTERED
                        )
                )

                .profileCreated(
                        repository.countByInstitutionIdAndStudentIdAndTypeAndDeletedFalse(
                                institutionId,
                                studentId,
                                TimelineEventType.PROFILE_CREATED
                        )
                )

                .transcriptCreated(
                        repository.countByInstitutionIdAndStudentIdAndTypeAndDeletedFalse(
                                institutionId,
                                studentId,
                                TimelineEventType.TRANSCRIPT_CREATED
                        )
                )

                .certificateIssued(
                        repository.countByInstitutionIdAndStudentIdAndTypeAndDeletedFalse(
                                institutionId,
                                studentId,
                                TimelineEventType.CERTIFICATE_ISSUED
                        )
                )

                .manualEvents(
                        repository.countByInstitutionIdAndStudentIdAndTypeAndDeletedFalse(
                                institutionId,
                                studentId,
                                TimelineEventType.MANUAL_EVENT
                        )
                )

                .build();
    }
}
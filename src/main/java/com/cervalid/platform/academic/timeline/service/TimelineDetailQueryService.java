package com.cervalid.platform.academic.timeline.service;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.service.StudentQueryService;
import com.cervalid.platform.academic.timeline.dto.response.TimelineEventResponse;
import com.cervalid.platform.academic.timeline.entity.TimelineEvent;
import com.cervalid.platform.academic.timeline.mapper.TimelineEventMapper;
import com.cervalid.platform.academic.timeline.repository.TimelineEventRepository;
import com.cervalid.platform.academic.timeline.validation.TimelineOwnershipValidator;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimelineDetailQueryService {

    private final TimelineEventRepository repository;
    private final SecurityContextService securityContextService;
    private final TimelineEventMapper mapper;
    private final StudentQueryService studentQueryService;
    private final TimelineOwnershipValidator ownershipValidator;

    @Transactional(readOnly = true)
    public TimelineEventResponse getByPublicId(
            UUID publicId) {

        Long institutionId =
                securityContextService.getInstitutionId();

        if (institutionId == null) {
            throw new IllegalStateException(
                    "Institution context is required"
            );
        }

        TimelineEvent event =
                repository.findByPublicIdAndDeletedFalse(publicId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Timeline event not found"));

        if (!event.getInstitutionId().equals(institutionId)) {
            throw new RuntimeException(
                    "Timeline event does not belong to institution");
        }

        // se obtiene el estudiante asociado al evento
        Student student =
                studentQueryService.getById(
                        event.getStudentId()
                );

        // validacion de estudiante debe pertenecer a la institucion
        ownershipValidator.validateStudentOwnership(
                student.getPublicId(),
                institutionId
        );

        return mapper.toResponse(
                event,
                student.getPublicId());
    }
}
package com.cervalid.platform.academic.timeline.service;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.timeline.dto.request.TimelineFilterRequest;
import com.cervalid.platform.academic.timeline.dto.response.TimelineEventResponse;
import com.cervalid.platform.academic.timeline.dto.response.TimelinePageResponse;
import com.cervalid.platform.academic.timeline.entity.TimelineEvent;
import com.cervalid.platform.academic.timeline.mapper.TimelineEventMapper;
import com.cervalid.platform.academic.timeline.repository.TimelineEventRepository;
import com.cervalid.platform.academic.timeline.repository.TimelineSpecification;
import com.cervalid.platform.academic.timeline.validation.TimelineOwnershipValidator;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineQueryService {

    private final TimelineEventRepository repository;
    private final SecurityContextService securityContextService;
    private final TimelineOwnershipValidator ownershipValidator;
    private final TimelineEventMapper mapper;

    public TimelinePageResponse getStudentTimeline(
            TimelineFilterRequest request,
            int page,
            int size) {

        // validar paginacion
        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page must be greater than or equal to 0"
            );
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Size must be between 1 and 100"
            );
        }

        // validar rango de fechas
        if (request.getFromDate() != null
                && request.getToDate() != null
                && request.getFromDate()
                .isAfter(request.getToDate())) {

            throw new IllegalArgumentException(
                    "fromDate must be before or equal to toDate"
            );
        }

        Long institutionId =
                securityContextService.getInstitutionId();

        if (institutionId == null) {
            throw new IllegalStateException(
                    "Institution context is required"
            );
        }

        UUID studentPublicId =
                request.getStudentPublicId();

        if (studentPublicId == null) {
            throw new IllegalArgumentException(
                    "studentPublicId is required"
            );
        }

        Student student =
                ownershipValidator.validateStudentOwnership(
                        studentPublicId,
                        institutionId
                );

        PageRequest pageable =
                PageRequest.of(page, size,
                        Sort.by(Sort.Direction.DESC,
                                "eventDate")
                );

        Page<TimelineEvent> result =
                repository.findAll(
                        TimelineSpecification.filter(
                                request,
                                institutionId,
                                student.getId()
                        ),
                        pageable
                );

        List<TimelineEventResponse> content =
                result.getContent()
                        .stream()
                        .map(event ->
                                mapper.toResponse(
                                        event,
                                        studentPublicId
                                )
                        )
                        .toList();

        return TimelinePageResponse.builder()
                .content(content)
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .first(result.isFirst())
                .last(result.isLast())
                .build();
    }
}
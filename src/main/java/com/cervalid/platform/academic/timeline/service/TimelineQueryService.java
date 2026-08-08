package com.cervalid.platform.academic.timeline.service;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.service.StudentQueryService;
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
import org.springframework.data.domain.Pageable;
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
            UUID studentPublicId,
            int page,
            int size) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Student student =
                ownershipValidator.validateStudentOwnership(
                        studentPublicId,
                        institutionId
                );

        Page<TimelineEvent> result = repository
                .findByStudentIdAndInstitutionIdAndDeletedFalseOrderByEventDateDesc(
                        student.getId(),
                        institutionId,
                        PageRequest.of(page, size)
                );

        List<TimelineEventResponse> content =
                result.getContent()
                        .stream()
                        //.map(mapper::toResponse)
                        .map(event ->
                                mapper.toResponse(
                                        event,
                                        studentPublicId))
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

    public Page<TimelineEventResponse> filterTimeline(
            TimelineFilterRequest request,
            Pageable pageable) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Student student =
                ownershipValidator.validateStudentOwnership(
                        request.getStudentPublicId(),
                        institutionId
                );

        Long studentId = student.getId();

        return repository.findAll(
                TimelineSpecification
                        .filter(request,
                                institutionId,
                                studentId),
                        pageable)
                .map(event ->
                        mapper.toResponse(
                                event,
                                request.getStudentPublicId()
                        )
                );
    }
}
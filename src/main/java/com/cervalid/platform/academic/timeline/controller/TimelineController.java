package com.cervalid.platform.academic.timeline.controller;

import com.cervalid.platform.academic.timeline.dto.request.CreateManualTimelineEventRequest;
import com.cervalid.platform.academic.timeline.dto.request.TimelineFilterRequest;
import com.cervalid.platform.academic.timeline.dto.response.TimelineEventResponse;
import com.cervalid.platform.academic.timeline.dto.response.TimelinePageResponse;
import com.cervalid.platform.academic.timeline.service.TimelineDeleteService;
import com.cervalid.platform.academic.timeline.service.TimelineDetailQueryService;
import com.cervalid.platform.academic.timeline.service.TimelineEventService;
import com.cervalid.platform.academic.timeline.service.TimelineQueryService;
import com.cervalid.platform.academic.timeline.summary.dto.response.TimelineSummaryResponse;
import com.cervalid.platform.academic.timeline.summary.service.TimelineSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/academic/timeline")
@RequiredArgsConstructor
public class TimelineController {

    private final TimelineEventService timelineEventService;
    private final TimelineQueryService timelineQueryService;
    private final TimelineDeleteService deleteService;
    private final TimelineSummaryService timelineSummaryService;
    private final TimelineDetailQueryService detailQueryService;

    @PostMapping("/manual")
    @PreAuthorize("hasAnyRole('INSTITUTION_ADMIN', 'INSTITUTION_SUBADMIN')")
    public TimelineEventResponse createManualEvent(
            @RequestBody CreateManualTimelineEventRequest request) {

        return timelineEventService
                .createManualEvent(request);
    }

    @GetMapping("/student/{studentPublicId}")
    public TimelinePageResponse getTimeline(
            @PathVariable UUID studentPublicId,
            @ModelAttribute TimelineFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        filter.setStudentPublicId(studentPublicId);

        return timelineQueryService.getStudentTimeline(
                filter,
                page,
                size
        );
    }

    @DeleteMapping("/{publicId}")
    @PreAuthorize("hasAnyRole('INSTITUTION_ADMIN', 'INSTITUTION_SUBADMIN')")
    public void delete(
            @PathVariable UUID publicId) {

        deleteService.delete(publicId);
    }

    @GetMapping("/{publicId}")
    public TimelineEventResponse getByPublicId(
            @PathVariable UUID publicId) {

        return detailQueryService.getByPublicId(publicId);
    }

    //resumen - dashboard
    @GetMapping("/student/{studentPublicId}/summary")
    public TimelineSummaryResponse getStudentSummary(
            @PathVariable UUID studentPublicId) {

        return timelineSummaryService.getStudentSummary(
                studentPublicId
        );
    }


}
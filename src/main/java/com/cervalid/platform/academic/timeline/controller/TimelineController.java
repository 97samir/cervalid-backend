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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public TimelineEventResponse createManualEvent(
            @RequestBody CreateManualTimelineEventRequest request) {

        return timelineEventService
                .createManualEvent(request);
    }

    @GetMapping("/student/{studentPublicId}")
    public TimelinePageResponse getTimeline(

            @PathVariable UUID studentPublicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return timelineQueryService.getStudentTimeline(
                studentPublicId,
                page,
                size
        );
    }

    @DeleteMapping("/{publicId}")
    public void delete(
            @PathVariable UUID publicId) {

        deleteService.delete(publicId);
    }

    @PostMapping("/search")
    public Page<TimelineEventResponse> search(
            @RequestBody TimelineFilterRequest request,
            Pageable pageable) {

        return timelineQueryService.filterTimeline(
                request,
                pageable);
    }

    @GetMapping("/{publicId}")
    public TimelineEventResponse getByPublicId(
            @PathVariable UUID publicId) {

        return detailQueryService.getByPublicId(publicId);
    }

    //resumen - dashboard
    @GetMapping("/summary")
    public TimelineSummaryResponse summary() {
        return timelineSummaryService.getSummary();
    }
}
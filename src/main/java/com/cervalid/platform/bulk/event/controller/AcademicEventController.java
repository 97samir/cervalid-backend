package com.cervalid.platform.bulk.event.controller;

import com.cervalid.platform.bulk.event.dto.AcademicEventRequest;
import com.cervalid.platform.bulk.event.dto.AcademicEventResponse;
import com.cervalid.platform.bulk.event.service.AcademicEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/academic-events")
@RequiredArgsConstructor
public class AcademicEventController {

    private final AcademicEventService eventService;

    @PostMapping
    public ResponseEntity<AcademicEventResponse> create(
            @RequestBody AcademicEventRequest request) {
        return ResponseEntity.ok(eventService.create(request));
    }

    // timeLine
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<AcademicEventResponse>> timeline(
            @PathVariable Long profileId) {
        return ResponseEntity.ok(eventService.getTimeline(profileId));
    }

}
package com.cervalid.platform.academic.timeline.summary.dto.response;

import lombok.*;

@Getter
@Builder
public class TimelineSummaryResponse {

    private Long totalEvents;
    private Long studentCreated;
    private Long profileCreated;
    private Long transcriptCreated;
    private Long certificateIssued;
    private Long manualEvents;
}
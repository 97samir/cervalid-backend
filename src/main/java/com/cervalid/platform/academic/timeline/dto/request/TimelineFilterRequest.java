package com.cervalid.platform.academic.timeline.dto.request;

import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class TimelineFilterRequest {

    private UUID studentPublicId;
    private TimelineEventType type;
    private TimelineEventSource source;
    private TimelineReferenceType referenceType;
    private String academicPeriod;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String keyword;
}
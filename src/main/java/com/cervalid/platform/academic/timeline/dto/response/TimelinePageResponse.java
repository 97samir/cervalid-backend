package com.cervalid.platform.academic.timeline.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
public class TimelinePageResponse {

    private List<TimelineEventResponse> content;

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}
package com.cervalid.platform.academic.student.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class StudentCreatedEvent {

    private UUID studentPublicId;
    private Long institutionMembershipId;
    private LocalDateTime timestamp;

}
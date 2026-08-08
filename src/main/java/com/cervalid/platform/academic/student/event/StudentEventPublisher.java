package com.cervalid.platform.academic.student.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishCreated(StudentCreatedEvent event) {
        publisher.publishEvent(event);
    }
}
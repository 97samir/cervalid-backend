package com.cervalid.platform.academic.timeline.service;

import com.cervalid.platform.academic.timeline.entity.TimelineEvent;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.repository.TimelineEventRepository;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimelineDeleteService {

    private final TimelineEventRepository repository;
    private final SecurityContextService securityContextService;

    @Transactional
    public void delete(UUID publicId) {

        Long institutionId =
                securityContextService.getInstitutionId();

        if (institutionId == null) {
            throw new IllegalStateException(
                    "Institution context is required"
            );
        }

        TimelineEvent event = repository
                .findByPublicIdAndDeletedFalse(publicId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Timeline event not found"));

        // para borrar solo el MANUAL_EVENT
        if (event.getType() != TimelineEventType.MANUAL_EVENT) {
            throw new RuntimeException(
                    "Only manual timeline events can be deleted"
            );
        }

        if (!event.getInstitutionId().equals(institutionId)) {

            throw new RuntimeException(
                    "Event does not belong to institution");
        }

        event.setDeleted(true);
        repository.save(event);
    }
}

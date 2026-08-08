package com.cervalid.platform.academic.timeline.service;

import com.cervalid.platform.academic.timeline.entity.TimelineEvent;
import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
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

        TimelineEvent event = repository
                .findByPublicIdAndDeletedFalse(publicId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Timeline event not found"));

        // para borrar solo el MANUAL_EVENT
        if (event.getSource() == TimelineEventSource.SYSTEM) {
            throw new RuntimeException(
                    "System events cannot be deleted");
        }

        if (!event.getInstitutionId().equals(institutionId)) {

            throw new RuntimeException(
                    "Event does not belong to institution");
        }

        event.setDeleted(true);

        repository.save(event);
    }
}

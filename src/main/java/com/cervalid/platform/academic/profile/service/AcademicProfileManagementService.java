package com.cervalid.platform.academic.profile.service;

import com.cervalid.platform.academic.profile.dto.request.UpdateAcademicProfileRequest;
import com.cervalid.platform.academic.profile.dto.response.AcademicProfileResponse;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.profile.mapper.ProfileMapper;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.academic.timeline.domain.TimelineMetadataBuilder;
import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.cervalid.platform.academic.timeline.service.TimelineEventService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AcademicProfileManagementService {

    private final AcademicProfileQueryService queryService;
    private final AcademicProfileRepository repository;
    private final TimelineEventService timelineEventService;
    private final TimelineMetadataBuilder metadataBuilder;
    private final ProfileMapper profileMapper;

    public AcademicProfileResponse update(
            UUID publicId,
            UpdateAcademicProfileRequest request) {

        AcademicProfile profile =
                queryService.getByPublicId(publicId);

        profile.setProgram(request.getProgram());
        profile.setFaculty(request.getFaculty());
        profile.setModality(request.getModality());
        profile.setCurrentCycle(request.getCurrentCycle());
        profile.setAdvisor(request.getAdvisor());
        profile.setActive(request.getActive());

        AcademicProfile saved =
                repository.save(profile);

        JsonNode metadata =
                metadataBuilder.build(
                        Map.of(
                                "program", saved.getProgram(),
                                "faculty", saved.getFaculty(),
                                "cycle", saved.getCurrentCycle()
                        ));

        timelineEventService.createEvent(
                saved.getInstitutionId(),
                saved.getStudentId(),
                TimelineEventType.PROFILE_UPDATED,
                TimelineEventSource.SYSTEM,
                "Academic profile updated",
                "Academic profile updated",
                saved.getPublicId(),
                TimelineReferenceType.PROFILE,
                metadata
        );

        return profileMapper.toResponse(saved);

    }

}
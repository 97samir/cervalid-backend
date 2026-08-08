package com.cervalid.platform.bulk.event.service;

import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.bulk.event.dto.AcademicEventRequest;
import com.cervalid.platform.bulk.event.dto.AcademicEventResponse;
import com.cervalid.platform.bulk.event.entity.AcademicEvent;
import com.cervalid.platform.bulk.event.repository.AcademicEventRepository;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.common.exception.UnauthorizedException;
import com.cervalid.platform.membership.service.InstitutionMembershipService;
import com.cervalid.platform.security.context.SecurityContextService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicEventService {

    private final AcademicEventRepository eventRepository;
    private final AcademicProfileRepository profileRepository;
    private final InstitutionMembershipService institutionMembershipService;
    private final SecurityContextService securityContextService;
    private final ObjectMapper objectMapper;

    // CREAR EVENTO
    @Transactional
    public AcademicEventResponse create(AcademicEventRequest request) {

        Long contextInstitutionId = securityContextService.getInstitutionId();

        AcademicProfile profile = profileRepository.findById(request.getAcademicProfileId())
                .orElseThrow(() -> new RuntimeException("Academic profile not found"));

        Long studentId = profile.getStudentId();
        Long institutionId = institutionMembershipService
                .getInstitutionId(studentId);

        if (!contextInstitutionId.equals(institutionId)) {
            throw new UnauthorizedException(
                    "Este perfil no pertenece a la institución");
        }

        AcademicEvent event = AcademicEvent.builder()
                .academicProfile(profile)
                .type(request.getType())
                .date(request.getDate())
                .title(request.getTitle())
                .description(request.getDescription())
                .metadataJson(objectMapper.valueToTree(request.getMetadata()))
                .build();

        AcademicEvent saved = eventRepository.save(event);

        return mapToResponse(saved);
    }

    // TIMELINE COMPLETO
    public List<AcademicEventResponse> getTimeline(Long profileId) {

        return eventRepository.findByAcademicProfile_IdOrderByDateAsc(profileId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AcademicEventResponse mapToResponse(AcademicEvent event) {

        Map<String, Object> metadata = null;

        if (event.getMetadataJson() != null) {
            metadata = objectMapper.convertValue(
                    event.getMetadataJson(),
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {}
            );
        }

        return AcademicEventResponse.builder()
                .id(event.getId())
                .type(event.getType())
                .date(event.getDate())
                .title(event.getTitle())
                .description(event.getDescription())
                .metadataJson(metadata)
                .build();
    }
}
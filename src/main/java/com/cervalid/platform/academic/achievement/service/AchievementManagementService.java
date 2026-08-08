package com.cervalid.platform.academic.achievement.service;

import com.cervalid.platform.academic.achievement.dto.request.CreateAchievementRequest;
import com.cervalid.platform.academic.achievement.dto.request.UpdateAchievementRequest;
import com.cervalid.platform.academic.achievement.dto.response.AchievementResponse;
import com.cervalid.platform.academic.achievement.entity.Achievement;
import com.cervalid.platform.academic.achievement.enums.AchievementStatus;
import com.cervalid.platform.academic.achievement.mapper.AchievementMapper;
import com.cervalid.platform.academic.achievement.repository.AchievementRepository;
import com.cervalid.platform.academic.achievement.validation.AchievementOwnershipValidator;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.security.context.SecurityContextService;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AchievementManagementService {

    private final AchievementRepository repository;
    private final SecurityContextService securityContextService;
    private final PublicIdGenerator publicIdGenerator;
    private final AchievementOwnershipValidator validator;
    private final AchievementTimelineService timelineService;
    private final AchievementMapper mapper;
    private final AchievementValidationService validationService;

    public AchievementResponse create(
            CreateAchievementRequest request){

        Long institutionId = securityContextService
                .getInstitutionId();

        Student student = validator
                .validateStudentOwnership(
                        request.getStudentPublicId(),
                        institutionId);

        validationService.validateCreate(
                student,
                request.getTitle()
        );

        Achievement achievement =
                Achievement.builder()
                        .publicId(publicIdGenerator.generate())
                        .institutionId(institutionId)
                        .studentId(student.getId())
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .type(request.getType())
                        .issuer(request.getIssuer())
                        .achievedDate(request.getAchievedDate())
                        .status(AchievementStatus.ACTIVE)
                        .build();

        Achievement saved = repository.save(achievement);

        timelineService.created(saved);

        return mapper.toResponse(
                saved,
                student.getPublicId());
    }

    public AchievementResponse update(
            UUID publicId,
            UpdateAchievementRequest request){

        Achievement achievement =
                getEntity(publicId);

        validationService.validateCanUpdate(achievement);

        achievement.setTitle(request.getTitle());
        achievement.setDescription(request.getDescription());
        achievement.setType(request.getType());
        achievement.setIssuer(request.getIssuer());
        achievement.setAchievedDate(request.getAchievedDate());

        Achievement saved = repository.save(achievement);

        timelineService.updated(saved);

        return mapper.toResponse(
                saved,
                null);
    }

    public void deactivate(UUID publicId){

        Achievement achievement = getEntity(publicId);

        validationService.validateCanDeactivate(achievement);
        achievement.setStatus(AchievementStatus.INACTIVE);
        repository.save(achievement);
        timelineService.deactivated(achievement);

    }

    private Achievement getEntity(UUID publicId){

        Long institutionId = securityContextService
                .getInstitutionId();

        return repository
                .findByPublicIdAndInstitutionId(
                        publicId,
                        institutionId)

                .orElseThrow(() ->
                        new RuntimeException(
                                "Achievement not found"));

    }

}
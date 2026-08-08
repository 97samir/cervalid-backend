package com.cervalid.platform.academic.achievement.service;

import com.cervalid.platform.academic.achievement.dto.request.CreateAchievementRequest;
import com.cervalid.platform.academic.achievement.dto.request.UpdateAchievementRequest;
import com.cervalid.platform.academic.achievement.dto.response.AchievementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementManagementService managementService;
    private final AchievementQueryService queryService;

    public AchievementResponse create(
            CreateAchievementRequest request){

        return managementService.create(request);
    }

    public AchievementResponse update(
            UUID publicId,
            UpdateAchievementRequest request){

        return managementService.update(
                publicId,
                request);
    }

    public void deactivate(
            UUID publicId){

        managementService.deactivate(
                publicId);
    }

    public AchievementResponse getByPublicId(
            UUID publicId){

        return queryService.getByPublicId(
                publicId);
    }

    public Page<AchievementResponse> list(
            UUID studentPublicId,
            String title,
            String type,
            String status,
            Pageable pageable){

        return queryService.list(
                studentPublicId,
                title,
                type,
                status,
                pageable);
    }

}
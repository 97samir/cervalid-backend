package com.cervalid.platform.academic.achievement.controller;
// logro o reconocimiento obtenido

import com.cervalid.platform.academic.achievement.dto.request.CreateAchievementRequest;
import com.cervalid.platform.academic.achievement.dto.request.UpdateAchievementRequest;
import com.cervalid.platform.academic.achievement.dto.response.AchievementResponse;
import com.cervalid.platform.academic.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/academic/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    @PostMapping("/students/{studentPublicId}")
    public AchievementResponse create(
            @PathVariable UUID studentPublicId,
            @RequestBody CreateAchievementRequest request) {

        request.setStudentPublicId(studentPublicId);

        return achievementService.create(request);
    }

    @GetMapping
    public Page<AchievementResponse> list(
            @RequestParam(required = false) UUID studentPublicId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String academicPeriod,
            Pageable pageable) {

        return achievementService.list(
                studentPublicId,
                title,
                type,
                status,
                academicPeriod,
                pageable);
    }

    @GetMapping("/{publicId}")
    public AchievementResponse getByPublicId(
            @PathVariable UUID publicId) {

        return achievementService.getByPublicId(publicId);
    }

    @PutMapping("/{publicId}")
    public AchievementResponse update(
            @PathVariable UUID publicId,
            @RequestBody UpdateAchievementRequest request) {

        return achievementService.update(publicId, request);
    }

    @DeleteMapping("/{publicId}")
    public void deactivate(@PathVariable UUID publicId) {

        achievementService.deactivate(publicId);
    }
}
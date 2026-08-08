package com.cervalid.platform.academic.profile.controller;

import com.cervalid.platform.academic.profile.dto.filter.AcademicProfileFilterRequest;
import com.cervalid.platform.academic.profile.dto.request.AcademicProfileRequest;
import com.cervalid.platform.academic.profile.dto.response.AcademicProfileResponse;
import com.cervalid.platform.academic.profile.dto.request.UpdateAcademicProfileRequest;
import com.cervalid.platform.academic.profile.service.AcademicProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/academic/profiles")
@RequiredArgsConstructor
public class AcademicProfileController {

    private final AcademicProfileService academicProfileService;

    // Crear profile académico manualmente - user existente
    @PostMapping("/students/{studentPublicId}/profile")
    public AcademicProfileResponse create(
            @PathVariable UUID studentPublicId,
            @RequestBody AcademicProfileRequest request) {

        return academicProfileService.create(
                studentPublicId,
                request);
    }

    // Obtener profile por studentId
    @GetMapping("/student/{studentPublicId}")
    public AcademicProfileResponse getByStudent(
            @PathVariable UUID studentPublicId) {
        return academicProfileService
                .getByStudentPublicId(studentPublicId);
    }

    @GetMapping("/{publicId}")
    public AcademicProfileResponse getByPublicId(
            @PathVariable UUID publicId) {

        return academicProfileService
                .getByPublicId(publicId);
    }

    @PutMapping("/{publicId}")
    public AcademicProfileResponse update(
            @PathVariable UUID publicId,
            @RequestBody UpdateAcademicProfileRequest request) {

        return academicProfileService
                .update(publicId, request);
    }

    @GetMapping
    public Page<AcademicProfileResponse> list(
            AcademicProfileFilterRequest filter,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return academicProfileService.findAll(
                filter,
                page,
                size);
    }
}
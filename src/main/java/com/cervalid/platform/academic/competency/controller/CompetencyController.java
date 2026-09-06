package com.cervalid.platform.academic.competency.controller;
// habilidad o competencia adquirida
import com.cervalid.platform.academic.competency.dto.filter.CompetencyFilterRequest;
import com.cervalid.platform.academic.competency.dto.request.CreateCompetencyRequest;
import com.cervalid.platform.academic.competency.dto.request.UpdateCompetencyRequest;
import com.cervalid.platform.academic.competency.dto.response.CompetencyResponse;
import com.cervalid.platform.academic.competency.service.CompetencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/academic/competencies")
@RequiredArgsConstructor
public class CompetencyController {

    private final CompetencyService competencyService;

    @PostMapping("/students/{studentPublicId}")
    public CompetencyResponse create(
            @PathVariable UUID studentPublicId,
            @Valid @RequestBody CreateCompetencyRequest request) {

        return competencyService.create(
                studentPublicId,
                request);
    }

    @GetMapping("/{publicId}")
    public CompetencyResponse getByPublicId(
            @PathVariable UUID publicId) {

        return competencyService.getByPublicId(
                publicId);
    }

    @GetMapping("/students/{studentPublicId}")
    public List<CompetencyResponse> getByStudent(
            @PathVariable UUID studentPublicId) {

        return competencyService.getByStudent(
                studentPublicId);
    }

    @GetMapping
    public Page<CompetencyResponse> list(
            CompetencyFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return competencyService.list(
                filter,
                page,
                size);
    }

    @PutMapping("/{publicId}")
    public CompetencyResponse update(
            @PathVariable UUID publicId,
            @Valid @RequestBody UpdateCompetencyRequest request) {

        return competencyService.update(
                publicId,
                request);
    }

    @DeleteMapping("/{publicId}")
    public void deactivate(
            @PathVariable UUID publicId) {

        competencyService.deactivate(publicId);
    }
}
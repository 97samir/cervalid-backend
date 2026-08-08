package com.cervalid.platform.academic.competency.service;

import com.cervalid.platform.academic.competency.dto.filter.CompetencyFilterRequest;
import com.cervalid.platform.academic.competency.dto.response.CompetencyResponse;
import com.cervalid.platform.academic.competency.entity.Competency;
import com.cervalid.platform.academic.competency.mapper.CompetencyMapper;
import com.cervalid.platform.academic.competency.repository.CompetencyRepository;
import com.cervalid.platform.academic.competency.repository.CompetencySpecification;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.service.StudentQueryService;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompetencyQueryService {

    private final CompetencyRepository repository;
    private final CompetencyMapper mapper;
    private final SecurityContextService securityContextService;
    private final StudentQueryService studentQueryService;

    public Competency getEntityByPublicId(UUID publicId) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return repository
                .findByPublicIdAndInstitutionId(
                        publicId,
                        institutionId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Competency not found"));
    }

    public CompetencyResponse getByPublicId(UUID publicId) {

        return mapper.toResponse(
                getEntityByPublicId(publicId));
    }

    public List<CompetencyResponse> getByStudent(
            UUID studentPublicId) {

        Student student =
                studentQueryService.getByPublicId(
                        studentPublicId);

        return repository
                .findByStudentIdAndInstitutionId(
                        student.getId(),
                        student.getInstitutionId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public Page<CompetencyResponse> list(
            CompetencyFilterRequest filter,
            int page,
            int size) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return repository.findAll(
                        CompetencySpecification.filter(
                                filter,
                                institutionId),
                        PageRequest.of(page, size))
                .map(mapper::toResponse);
    }

}
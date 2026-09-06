package com.cervalid.platform.academic.competency.service;

import com.cervalid.platform.academic.competency.dto.filter.CompetencyFilterRequest;
import com.cervalid.platform.academic.competency.dto.request.CreateCompetencyRequest;
import com.cervalid.platform.academic.competency.dto.request.UpdateCompetencyRequest;
import com.cervalid.platform.academic.competency.dto.response.CompetencyResponse;
import com.cervalid.platform.academic.competency.entity.Competency;
import com.cervalid.platform.academic.competency.mapper.CompetencyMapper;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.service.StudentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompetencyService {

    private final CompetencyManagementService managementService;
    private final CompetencyQueryService queryService;
    private final CompetencyMapper mapper;
    private final StudentQueryService studentQueryService;

    public CompetencyResponse create(
            UUID studentPublicId,
            CreateCompetencyRequest request) {

        Competency competency =
                managementService.create(
                        studentPublicId,
                        request);

        return mapper.toResponse(
                competency,
                studentPublicId);
    }

    public CompetencyResponse getByPublicId(
            UUID publicId) {

        return queryService.getByPublicId(
                publicId);
    }

    public List<CompetencyResponse> getByStudent(
            UUID studentPublicId) {

        return queryService.getByStudent(
                studentPublicId);
    }

    public Page<CompetencyResponse> list(
            CompetencyFilterRequest filter,
            int page,
            int size) {

        return queryService.list(
                filter,
                page,
                size);
    }

    public CompetencyResponse update(
            UUID publicId,
            UpdateCompetencyRequest request) {

        Competency competency =
                managementService.update(
                        publicId,
                        request);

        Student student =
                studentQueryService.getById(
                        competency.getStudentId());

        return mapper.toResponse(
                competency,
                student.getPublicId());
    }

    public void deactivate(
            UUID publicId) {

        managementService.deactivate(
                publicId);
    }
}
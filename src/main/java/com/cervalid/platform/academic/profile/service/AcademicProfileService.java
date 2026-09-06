package com.cervalid.platform.academic.profile.service;
// estado academico dinamico
// a diferencia de STUDENT - identidad academica
import com.cervalid.platform.academic.profile.dto.filter.AcademicProfileFilterRequest;
import com.cervalid.platform.academic.profile.dto.internal.CreateAcademicProfileCommand;
import com.cervalid.platform.academic.profile.dto.request.AcademicProfileRequest;
import com.cervalid.platform.academic.profile.dto.response.AcademicProfileResponse;
import com.cervalid.platform.academic.profile.dto.request.UpdateAcademicProfileRequest;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.service.StudentQueryService;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AcademicProfileService {

    private final AcademicProfileRepository repository;
    private final StudentQueryService studentQueryService;
    private final SecurityContextService securityContextService;
    private final AcademicProfileDetailQueryService detailQueryService;
    private final AcademicProfileManagementService managementService;
    private final AcademicProfileSearchService searchService;

    public AcademicProfileResponse create(
            UUID studentPublicId,
            AcademicProfileRequest request) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Student student =
                studentQueryService.getByPublicId(studentPublicId);

        if (!student.getInstitutionId().equals(institutionId)) {
            throw new RuntimeException("Student does not belong to institution");
        }

        CreateAcademicProfileCommand command =
                CreateAcademicProfileCommand.builder()
                        .studentId(student.getId())
                        .institutionId(institutionId)
                        .program(request.getProgram())
                        .faculty(request.getFaculty())
                        .modality(request.getModality())
                        .currentCycle(request.getCurrentCycle())
                        .advisor(request.getAdvisor())
                        .academicPeriod(request.getAcademicPeriod())
                        .build();

        return managementService.create(command);
    }

    public AcademicProfileResponse getByStudentPublicId(
            UUID studentPublicId) {

        return detailQueryService.getByStudentPublicId(
                studentPublicId);
    }

    public AcademicProfileResponse getByPublicId(
            UUID publicId) {

        return detailQueryService.getByPublicId(
                publicId);
    }

    public AcademicProfileResponse update(
            UUID publicId,
            UpdateAcademicProfileRequest request) {

        return managementService.update(
                publicId, request);
    }

    public Page<AcademicProfileResponse> findAll(
            AcademicProfileFilterRequest filter,
            int page,
            int size) {

        return searchService.search(filter, page, size);
    }
}
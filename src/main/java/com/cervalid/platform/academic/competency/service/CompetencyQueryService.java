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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompetencyQueryService {

    private final CompetencyRepository repository;
    private final CompetencyMapper mapper;
    private final SecurityContextService securityContextService;
    private final StudentQueryService studentQueryService;

    public Competency getEntityByPublicId(
            UUID publicId) {

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

    public CompetencyResponse getByPublicId(
            UUID publicId) {

        Competency competency =
                getEntityByPublicId(publicId);

        Student student =
                studentQueryService.getById(
                        competency.getStudentId());

        validateStudentInstitution(
                student,
                competency.getInstitutionId());

        return mapper.toResponse(
                competency,
                student.getPublicId());
    }

    public List<CompetencyResponse> getByStudent(
            UUID studentPublicId) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Student student =
                studentQueryService.getByPublicId(
                        studentPublicId);

        validateStudentInstitution(
                student,
                institutionId);

        return repository
                .findByStudentIdAndInstitutionId(
                        student.getId(),
                        institutionId)
                .stream()
                .map(competency ->
                        mapper.toResponse(
                                competency,
                                student.getPublicId()))
                .toList();
    }

    public Page<CompetencyResponse> list(
            CompetencyFilterRequest filter,
            int page,
            int size) {

        Long institutionId = securityContextService.getInstitutionId();
        Long studentId = null;
        Student student = null;

        if (filter.getStudentPublicId() != null) {

            student =
                    studentQueryService.getByPublicId(
                            filter.getStudentPublicId());

            validateStudentInstitution(
                    student,
                    institutionId);

            studentId = student.getId();
        }

        final Student resolvedStudent = student;

        return repository.findAll(
                        CompetencySpecification.filter(
                                filter,
                                institutionId,
                                studentId),
                        PageRequest.of(
                                page,
                                size))
                .map(competency -> {

                    Student currentStudent =
                            resolvedStudent;

                    if (currentStudent == null ||
                            !currentStudent.getId()
                                    .equals(
                                            competency.getStudentId())) {

                        currentStudent =
                                studentQueryService.getById(
                                        competency.getStudentId());
                    }

                    validateStudentInstitution(
                            currentStudent,
                            institutionId);

                    return mapper.toResponse(
                            competency,
                            currentStudent.getPublicId());
                });
    }

    private void validateStudentInstitution(
            Student student,
            Long institutionId) {

        if (!student.getInstitutionId()
                .equals(institutionId)) {

            throw new RuntimeException(
                    "Student does not belong to the current institution");
        }
    }
}
package com.cervalid.platform.academic.competency.service;

import com.cervalid.platform.academic.competency.dto.request.CreateCompetencyRequest;
import com.cervalid.platform.academic.competency.dto.request.UpdateCompetencyRequest;
import com.cervalid.platform.academic.competency.entity.Competency;
import com.cervalid.platform.academic.competency.repository.CompetencyRepository;
import com.cervalid.platform.academic.student.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompetencyValidationService {

    private final CompetencyRepository repository;

    public void validateCreate(
            Student student,
            CreateCompetencyRequest request) {

        validateName(request.getName());

        if (request.getLevel() == null) {
            throw new IllegalArgumentException(
                    "Competency level is required");
        }

        boolean exists =
                repository.existsByStudentIdAndNameAndLevel(
                        student.getId(),
                        normalize(request.getName()),
                        request.getLevel());

        if (exists) {
            throw new IllegalArgumentException(
                    "Competency already exists");
        }
    }

    public void validateUpdate(
            Competency competency,
            UpdateCompetencyRequest request) {

        validateActive(competency);

        validateName(request.getName());

        if (request.getLevel() == null) {
            throw new IllegalArgumentException(
                    "Competency level is required");
        }

        boolean exists =
                repository
                        .existsByStudentIdAndNameAndLevelAndIdNot(
                                competency.getStudentId(),
                                normalize(request.getName()),
                                request.getLevel(),
                                competency.getId());

        if (exists) {
            throw new IllegalArgumentException(
                    "Another competency with the same name and level already exists");
        }
    }

    public void validateDeactivate(
            Competency competency) {

        validateActive(competency);
    }

    private void validateActive(
            Competency competency) {

        if (!competency.getStatus().isActive()) {

            throw new IllegalArgumentException(
                    "Only active competency can be modified");
        }
    }

    private void validateName(String name) {

        if (name == null || name.isBlank()) {

            throw new IllegalArgumentException(
                    "Competency name is required");
        }
    }

    private String normalize(String value) {
        return value.trim();
    }
}
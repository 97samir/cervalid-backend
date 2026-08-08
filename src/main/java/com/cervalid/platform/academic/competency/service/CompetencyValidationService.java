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

        if (repository.existsByStudentIdAndNameAndLevel(
                student.getId(),
                request.getName(),
                request.getLevel())) {

            throw new RuntimeException(
                    "Competency already exists");
        }
    }

    public void validateUpdate(
            Competency competency,
            UpdateCompetencyRequest request) {

        if (competency.getStatus().isInactive()) {
            throw new RuntimeException(
                    "Inactive competency cannot be updated");
        }
    }

    public void validateDeactivate(
            Competency competency) {

        if (competency.getStatus().isInactive()) {

            throw new RuntimeException(
                    "Competency already inactive");
        }
    }

}
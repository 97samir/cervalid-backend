package com.cervalid.platform.academic.achievement.validation;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AchievementOwnershipValidator {

    private final StudentRepository studentRepository;

    public Student validateStudentOwnership(
            UUID studentPublicId,
            Long institutionId) {

        Student student =
                studentRepository.findByPublicId(studentPublicId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Student not found"));

        if (!student.getInstitutionId()
                .equals(institutionId)) {

            throw new RuntimeException(
                    "Student does not belong to institution");
        }

        return student;
    }
}
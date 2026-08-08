package com.cervalid.platform.academic.timeline.validation;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimelineOwnershipValidator {

    private final StudentRepository studentRepository;

    public Student validateStudentOwnership(
            UUID studentPublicId,
            Long institutionId) {

        return studentRepository
                .findByPublicIdAndInstitutionId(
                        studentPublicId,
                        institutionId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student does not belong to institution"
                        ));
    }
}

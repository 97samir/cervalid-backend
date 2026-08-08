package com.cervalid.platform.academic.certificate.validation;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateOwnershipValidator {

    private final StudentRepository repository;

    public Student validateStudentOwnership(
            UUID studentPublicId,
            Long institutionId) {

        Student student =
                repository.findByPublicId(studentPublicId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Student not found"));

        if (!student.getInstitutionId().equals(institutionId)) {

            throw new RuntimeException(
                    "Student does not belong to institution");
        }

        return student;
    }

}
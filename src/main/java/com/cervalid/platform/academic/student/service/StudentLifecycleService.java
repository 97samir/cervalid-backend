package com.cervalid.platform.academic.student.service;
// obtener estado (activo - inactivo) cuando el usuario active su cuenta

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.enums.StudentStatus;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentLifecycleService {

    private final StudentRepository studentRepository;

    public void activateStudent(
            Long membershipId) {

        Student student =
                studentRepository
                        .findByInstitutionMembershipId(
                                membershipId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Student not found"));

        if (student.getStatus()
                == StudentStatus.PENDING_ACTIVATION) {

            student.setStatus(
                    StudentStatus.ACTIVE);
        }
    }
}
package com.cervalid.platform.academic.student.service;

import com.cervalid.platform.academic.student.dto.request.UpdateStudentRequest;
import com.cervalid.platform.academic.student.dto.response.StudentResponse;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.enums.StudentStatus;
import com.cervalid.platform.academic.student.mapper.StudentMapper;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentManagementService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentMapper mapper;

    public StudentResponse update(
            UUID publicId,
            UpdateStudentRequest request) {

        Student student = studentRepository
                .findByPublicId(publicId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student not found"));

        if (request.getStatus() != null) {
            student.setStatus(request.getStatus());
        }

        if (request.getGraduationDate() != null) {
            student.setGraduationDate(
                    request.getGraduationDate());
        }

        Student saved = studentRepository.save(student);

        User user = userRepository
                .findById(saved.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return mapper.toResponse(saved, user);
    }

    public void deactivate(UUID publicId) {

        Student student = studentRepository
                .findByPublicId(publicId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student not found"));

        //student.setDeleted(true);
        student.setStatus(StudentStatus.INACTIVE);
        studentRepository.save(student);
    }

    @Transactional
    public void activateStudent(
            Long institutionMembershipId) {

        Student student = studentRepository
                .findByInstitutionMembershipId(
                        institutionMembershipId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student not found"));

        if (student.getStatus() ==
                StudentStatus.PENDING_ACTIVATION) {
            student.setStatus(StudentStatus.ACTIVE);
        }

        studentRepository.save(student);
    }
}

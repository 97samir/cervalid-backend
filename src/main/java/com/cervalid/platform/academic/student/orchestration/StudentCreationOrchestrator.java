package com.cervalid.platform.academic.student.orchestration;

import com.cervalid.platform.academic.student.dto.internal.CreateStudentCommand;
import com.cervalid.platform.academic.student.dto.request.CreateStudentRequest;
import com.cervalid.platform.academic.student.dto.response.StudentResponse;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.mapper.StudentMapper;
import com.cervalid.platform.academic.student.service.StudentService;
import com.cervalid.platform.security.context.SecurityContextService;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentCreationOrchestrator {

    private final UserRepository userRepository;
    private final InstitutionUserRepository membershipRepository;
    private final SecurityContextService securityContextService;
    private final StudentService studentService;
    private final StudentMapper studentMapper;

    public StudentResponse create(
            CreateStudentRequest request) {

        Long institutionId =
                securityContextService.getInstitutionId();

        User user =
                userRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"));

        InstitutionUser membership =
                membershipRepository
                        .findByUserIdAndInstitutionId(
                                user.getId(),
                                institutionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Membership not found"));

        CreateStudentCommand command =

                CreateStudentCommand.builder()

                        .userId(user.getId())
                        .institutionId(institutionId)
                        .institutionMembershipId(membership.getId())
                        .studentCode(request.getStudentCode())
                        .admissionDate(request.getAdmissionDate())
                        .graduationDate(request.getGraduationDate())
                        .build();

        Student student =
                studentService.createActiveStudent(command);

        return studentMapper.toResponse(
                student,
                user
        );
    }
}
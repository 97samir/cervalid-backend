package com.cervalid.platform.academic.student.orchestration;

import com.cervalid.platform.academic.profile.dto.request.AcademicProfileRequest;
import com.cervalid.platform.academic.profile.service.AcademicProfileService;
import com.cervalid.platform.academic.student.dto.internal.CreateStudentCommand;
import com.cervalid.platform.academic.student.dto.request.RegisterStudentRequest;
import com.cervalid.platform.academic.student.dto.response.StudentRegistrationResponse;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.service.StudentService;
import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.invitation.service.InvitationService;
import com.cervalid.platform.membership.dto.CreateMembershipRequest;
import com.cervalid.platform.membership.dto.MembershipResponse;
import com.cervalid.platform.membership.service.InstitutionMembershipService;
import com.cervalid.platform.security.context.SecurityContextService;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.provisioning.dto.UserProvisioningRequest;
import com.cervalid.platform.user.provisioning.service.UserProvisioningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional // registra todo o nada
public class StudentRegistrationOrchestrator {

    private final UserProvisioningService userProvisioningService;
    private final InstitutionMembershipService membershipService;
    private final StudentService studentService;
    private final AcademicProfileService academicProfileService;
    private final InvitationService invitationService;
    private final SecurityContextService securityContextService;

    public StudentRegistrationResponse register(
            RegisterStudentRequest request) {

        Long institutionId =
                securityContextService
                        .getInstitutionId();
        // USER
        User user =
                userProvisioningService.provisionUser(
                        buildUserRequest(request));

        // MEMBERSHIP
        MembershipResponse membership =
                membershipService.createMembership(
                        buildMembershipRequest(
                                institutionId,
                                user
                        )
                );

        // STUDENT
        CreateStudentCommand command =
                buildStudentCommand(
                        request,
                        membership,
                        user,
                        institutionId);

        Student student =
                studentService.createPendingStudent(
                        command);

        // PROFILE
        academicProfileService.create(
                student.getPublicId(),
                buildProfileRequest(request)
        );

        // enviar email
        invitationService.createActivationInvitation(
                user,
                institutionId
        );

        // RESPONSE
        return buildResponse(user, student);
    }

    private StudentRegistrationResponse buildResponse(
            User user, Student student) {

        return StudentRegistrationResponse.builder()
                .studentPublicId(student.getPublicId())
                .email(user.getEmail())
                .studentCode(student.getStudentCode())
                .status(student.getStatus().name())
                .build();
    }

    private UserProvisioningRequest buildUserRequest(
            RegisterStudentRequest request) {

        UserProvisioningRequest dto =
                new UserProvisioningRequest();

        dto.setEmail(request.getEmail());

        return dto;
    }

    private CreateMembershipRequest buildMembershipRequest(
            Long institutionId, User user) {

        CreateMembershipRequest dto =
                new CreateMembershipRequest();

        dto.setUserId(user.getId());
        dto.setInstitutionId(institutionId);
        dto.setRole(RoleName.STUDENT);

        return dto;
    }

    private AcademicProfileRequest buildProfileRequest(
            RegisterStudentRequest request) {

        AcademicProfileRequest dto =
                new AcademicProfileRequest();

        //dto.setStudentId(student.getId());
        //dto.setInstitutionId(institutionId);
        dto.setProgram(request.getProgram());
        dto.setFaculty(request.getFaculty());
        dto.setModality(request.getModality());
        dto.setCurrentCycle(request.getCurrentCycle());
        //dto.setAdvisor(request.getAdvisor());
        //dto.setActive(true);

        return dto;
    }

    private CreateStudentCommand buildStudentCommand(
            RegisterStudentRequest request,
            MembershipResponse membership,
            User user,
            Long institutionId) {

        return CreateStudentCommand.builder()
                .userId(user.getId())
                .institutionId(institutionId)
                .institutionMembershipId(membership.getInstitutionUserId())
                .studentCode(request.getStudentCode())
                .admissionDate(request.getAdmissionDate())
                .graduationDate(request.getGraduationDate())
                .build();
    }
}
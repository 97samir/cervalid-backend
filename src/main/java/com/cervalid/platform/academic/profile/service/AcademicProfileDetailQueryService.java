package com.cervalid.platform.academic.profile.service;

import com.cervalid.platform.academic.profile.dto.response.AcademicProfileResponse;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.profile.mapper.ProfileMapper;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.service.StudentQueryService;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcademicProfileDetailQueryService {

    private final AcademicProfileQueryService profileQueryService;
    private final StudentQueryService studentQueryService;
    private final ProfileMapper profileMapper;
    private final SecurityContextService securityContextService;

    public AcademicProfileResponse getByPublicId(
            UUID publicId) {

        AcademicProfile profile =
                profileQueryService
                        .getByPublicId(publicId);

        return profileMapper.toResponse(profile);
    }

    public AcademicProfileResponse getByStudentPublicId(
            UUID studentPublicId) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Student student =
                studentQueryService.getByPublicId(
                        studentPublicId);

        if (!student.getInstitutionId()
                .equals(institutionId)) {

            throw new RuntimeException(
                    "Student does not belong to institution");
        }

        AcademicProfile profile =
                profileQueryService
                        .getByStudentAndInstitution(
                                student.getId(),
                                institutionId);

        return profileMapper.toResponse(profile);
    }

}
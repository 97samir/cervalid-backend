package com.cervalid.platform.academic.profile.domain;

import com.cervalid.platform.academic.profile.dto.internal.CreateAcademicProfileCommand;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileFactory {

    private final PublicIdGenerator publicIdGenerator;

    public AcademicProfile create(
            CreateAcademicProfileCommand command) {

        return AcademicProfile.builder()

                .publicId(publicIdGenerator.generate())
                .studentId(command.getStudentId())
                .institutionId(command.getInstitutionId())
                .program(command.getProgram())
                .faculty(command.getFaculty())
                .modality(command.getModality())
                .curriculumVersion("v1")
                .currentCycle(command.getCurrentCycle())
                .advisor(command.getAdvisor())
                .active(true)
                .build();

    }

}
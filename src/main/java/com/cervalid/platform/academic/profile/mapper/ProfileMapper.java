package com.cervalid.platform.academic.profile.mapper;

import com.cervalid.platform.academic.profile.dto.response.AcademicProfileResponse;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public AcademicProfileResponse toResponse(
            AcademicProfile profile) {

        return AcademicProfileResponse.builder()
                .publicId(profile.getPublicId())
                .program(profile.getProgram())
                .faculty(profile.getFaculty())
                .modality(profile.getModality())
                .currentCycle(profile.getCurrentCycle())
                .advisor(profile.getAdvisor())
                .active(profile.getActive())
                .academicPeriod(profile.getAcademicPeriod())
                .build();
    }

}
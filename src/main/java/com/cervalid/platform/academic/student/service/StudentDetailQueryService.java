package com.cervalid.platform.academic.student.service;

import com.cervalid.platform.academic.profile.mapper.ProfileMapper;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.academic.student.dto.response.StudentDetailResponse;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.mapper.StudentMapper;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentDetailQueryService {

    private final StudentQueryService studentQueryService;
    private final AcademicProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final StudentMapper studentMapper;
    private final ProfileMapper profileMapper;

    public StudentDetailResponse getDetail(UUID publicId) {

        Student student =
                studentQueryService.getByPublicId(publicId);

        // traer al user
        User user = userRepository
                .findById(student.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        StudentDetailResponse response =
                studentMapper.toDetail(student, user);

        profileRepository
                .findByStudentId(student.getId())
                .ifPresent(profile ->
                        response.setProfile(
                                profileMapper.toResponse(
                                        profile)
                        )
                );

        return response;
    }
}

package com.cervalid.platform.academic.achievement.service;

import com.cervalid.platform.academic.achievement.dto.response.AchievementResponse;
import com.cervalid.platform.academic.achievement.entity.Achievement;
import com.cervalid.platform.academic.achievement.mapper.AchievementMapper;
import com.cervalid.platform.academic.achievement.repository.AchievementRepository;
import com.cervalid.platform.academic.achievement.specification.AchievementSpecification;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.security.context.SecurityContextService;


import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AchievementQueryService {

    private final AchievementRepository repository;
    private final StudentRepository studentRepository;
    private final SecurityContextService securityContextService;
    private final AchievementMapper mapper;

    public AchievementResponse getByPublicId(
            UUID publicId){

        Long institutionId =
                securityContextService.getInstitutionId();

        Achievement achievement =
                repository.findByPublicIdAndInstitutionId(
                                publicId,
                                institutionId)

                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Achievement not found"));

        Student student = studentRepository
                .findById(achievement.getStudentId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Student not found"));

        return mapper.toResponse(
                achievement,
                student.getPublicId());
    }

    public Page<AchievementResponse> list(
            UUID studentPublicId,
            String title,
            String type,
            String status,
            Pageable pageable){

        Long institutionId =
                securityContextService.getInstitutionId();

        Long studentId = null;

        if(studentPublicId != null){

            Student student =
                    studentRepository.findByPublicId(
                                    studentPublicId)

                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Student not found"));

            if(!student.getInstitutionId()
                    .equals(institutionId)){

                throw new RuntimeException(
                        "Unauthorized");
            }

            studentId = student.getId();
        }

        return repository.findAll(
                        AchievementSpecification.filter(
                                institutionId,
                                studentId,
                                title,
                                type,
                                status),

                        pageable)

                .map(a -> {

                    Student student =
                            studentRepository.findById(
                                            a.getStudentId())

                                    .orElseThrow();

                    return mapper.toResponse(
                            a,
                            student.getPublicId());
                });
    }
}
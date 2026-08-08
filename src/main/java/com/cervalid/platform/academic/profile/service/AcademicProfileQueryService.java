package com.cervalid.platform.academic.profile.service;

import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcademicProfileQueryService {

    private final AcademicProfileRepository repository;

    public AcademicProfile getByPublicId(UUID publicId) {

        return repository.findByPublicId(publicId)
                .orElseThrow(() ->
                        new RuntimeException("Academic profile not found"));
    }

    public AcademicProfile getByStudentAndInstitution(
            Long studentId,
            Long institutionId) {

        return repository.findByStudentIdAndInstitutionId(
                        studentId,
                        institutionId)
                .orElseThrow(() ->
                        new RuntimeException("Academic profile not found"));
    }
}
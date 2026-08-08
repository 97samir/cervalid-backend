package com.cervalid.platform.academic.profile.service;

import com.cervalid.platform.academic.profile.dto.filter.AcademicProfileFilterRequest;
import com.cervalid.platform.academic.profile.dto.response.AcademicProfileResponse;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.profile.mapper.ProfileMapper;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.academic.profile.specification.AcademicProfileSpecification;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcademicProfileSearchService {

    private final AcademicProfileRepository repository;
    private final SecurityContextService securityContextService;
    private final ProfileMapper profileMapper;

    public Page<AcademicProfileResponse> search(
            AcademicProfileFilterRequest filter,
            int page,
            int size) {

        Long institutionId =
                securityContextService.getInstitutionId();

        Specification<AcademicProfile> spec =
                AcademicProfileSpecification.build(
                        filter,
                        institutionId);

        return repository
                .findAll(
                        spec,
                        PageRequest.of(page, size))
                .map(profileMapper::toResponse);

    }

}
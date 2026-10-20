package com.cervalid.platform.academic.student.service;

import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.academic.student.dto.filter.StudentFilterRequest;
import com.cervalid.platform.academic.student.dto.response.StudentResponse;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.mapper.StudentMapper;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.academic.student.specification.StudentSpecification;
import com.cervalid.platform.security.user.CustomUserDetails;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentSearchService {

    private final StudentRepository repository;
    private final UserRepository userRepository;
    private final AcademicProfileRepository profileRepository;
    private final StudentMapper studentMapper;
    private final InstitutionUserRepository institutionUserRepository;


    public Page<StudentResponse> search(
            StudentFilterRequest filter,
            int page,
            int size) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal()
                instanceof CustomUserDetails userDetails)) {

            throw new org.springframework.security.access.AccessDeniedException(
                    "No existe un contexto institucional autenticado"
            );
        }

        Long institutionUserId = userDetails.getInstitutionUserId();

        if (institutionUserId == null) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "El usuario no tiene una membresía institucional activa"
            );
        }

        var membership = institutionUserRepository
                .findByIdWithInstitutionAndRole(institutionUserId)
                .orElseThrow(() ->
                        new org.springframework.security.access.AccessDeniedException(
                                "Membresía institucional no válida"
                        )
                );

        if (!membership.isActive()
                || !membership.getUser().getId()
                .equals(userDetails.getUserId())) {

            throw new org.springframework.security.access.AccessDeniedException(
                    "La membresía institucional no está autorizada"
            );
        }

        Long institutionId = membership.getInstitution().getId();

        Pageable pageable = PageRequest.of(page, size);

        Specification<Student> spec =
                StudentSpecification.build(filter, institutionId);

        Page<Student> students =
                repository.findAll(spec, pageable);

        List<Long> studentIds = students.getContent()
                .stream()
                .map(Student::getId)
                .toList();

        List<AcademicProfile> profiles = studentIds.isEmpty()
                ? List.of()
                : profileRepository.findAllByStudentIdIn(studentIds);

        Map<Long, AcademicProfile> profileMap = profiles.stream()
                .collect(Collectors.toMap(
                        AcademicProfile::getStudentId,
                        profile -> profile
                ));

        return students.map(student -> {

            User user = userRepository.findById(student.getUserId())
                    .orElseThrow(() ->
                            new RuntimeException("User not found")
                    );

            AcademicProfile profile = profileMap.get(student.getId());

            return studentMapper.toResponse(student, user, profile);
        });
    }

}

package com.cervalid.platform.academic.student.service;

import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.academic.student.dto.filter.StudentFilterRequest;
import com.cervalid.platform.academic.student.dto.response.StudentResponse;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.mapper.StudentMapper;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.academic.student.specification.StudentSpecification;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<StudentResponse> search(
            StudentFilterRequest filter,
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        Specification<Student> spec =
                StudentSpecification.build(filter);

        Page<Student> students =
                repository.findAll(spec, pageable);

        List<Long> studentIds =
                students.getContent()
                        .stream()
                        .map(Student::getId)
                        .toList();

        List<AcademicProfile> profiles =
                studentIds.isEmpty()
                        ? List.of()
                        : profileRepository
                        .findAllByStudentIdIn(studentIds);

        Map<Long, AcademicProfile> profileMap =
                profiles.stream()
                        .collect(Collectors.toMap(
                                AcademicProfile::getStudentId,
                                profile -> profile
                        ));

        return students.map(student -> {

            User user =
                    userRepository
                            .findById(student.getUserId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "User not found"
                                    )
                            );

            AcademicProfile profile =
                    profileMap.get(student.getId());

            return studentMapper.toResponse(
                    student,
                    user,
                    profile
            );
        });
    }
}

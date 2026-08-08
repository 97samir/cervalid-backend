package com.cervalid.platform.academic.student.service;

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

@Service
@RequiredArgsConstructor
public class StudentSearchService {

    private final StudentRepository repository;
    private final UserRepository userRepository;
    private final StudentMapper studentMapper;

    public Page<StudentResponse> search(
            StudentFilterRequest filter,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<Student> spec =
                StudentSpecification.build(filter);

        return repository.findAll(spec, pageable)
                .map(student -> {
                    User user = userRepository
                            .findById(student.getUserId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "User not found"));

                    return studentMapper.toResponse(student, user);
                });
    }
}

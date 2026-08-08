package com.cervalid.platform.academic.student.service;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.exception.StudentNotFoundException;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentQueryService {

    private final StudentRepository repository;

    public Student getByPublicId(UUID publicId) {

        return repository.findByPublicId(publicId)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found")
                );
    }

    public Student getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found"));
    }
}
package com.cervalid.platform.academic.student.controller;

import com.cervalid.platform.academic.student.dto.filter.StudentFilterRequest;
import com.cervalid.platform.academic.student.dto.request.CreateStudentRequest;
import com.cervalid.platform.academic.student.dto.request.RegisterStudentRequest;
import com.cervalid.platform.academic.student.dto.request.UpdateStudentRequest;
import com.cervalid.platform.academic.student.dto.response.StudentDetailResponse;
import com.cervalid.platform.academic.student.dto.response.StudentRegistrationResponse;
import com.cervalid.platform.academic.student.dto.response.StudentResponse;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.mapper.StudentMapper;
import com.cervalid.platform.academic.student.orchestration.StudentCreationOrchestrator;
import com.cervalid.platform.academic.student.orchestration.StudentRegistrationOrchestrator;
import com.cervalid.platform.academic.student.service.StudentDetailQueryService;
import com.cervalid.platform.academic.student.service.StudentManagementService;
import com.cervalid.platform.academic.student.service.StudentSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/academic/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRegistrationOrchestrator orchestrator;
    private final StudentDetailQueryService detailQueryService;
    private final StudentManagementService studentManagementService;
    private final StudentSearchService studentSearchService;
    private final StudentCreationOrchestrator creationOrchestrator;

    // solo crea a estudiante - para usuario existente
    @PostMapping
    public StudentResponse create(
            @RequestBody CreateStudentRequest request) {
        return creationOrchestrator.create(request);
    }

    // crea user global y perfil estudiane
    @PostMapping("/register")
    public StudentRegistrationResponse register(
            @RequestBody RegisterStudentRequest request) {
        return orchestrator.register(request);
    }

    @GetMapping("/{publicId}")
    public StudentDetailResponse get(
            @PathVariable UUID publicId) {

        return detailQueryService.getDetail(publicId);
    }

    // editar - actualizar
    @PutMapping("/{publicId}")
    public StudentResponse update(
            @PathVariable UUID publicId,
            @RequestBody UpdateStudentRequest request) {

        return studentManagementService
                .update(publicId, request);
    }

    // desactivar estudiantes
    @DeleteMapping("/{publicId}")
    public void delete(
            @PathVariable UUID publicId) {

        studentManagementService.deactivate(publicId);
    }

    @GetMapping
    public Page<StudentResponse> search(
            StudentFilterRequest filter,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return studentSearchService.search(
                filter,
                page,
                size);
    }
}

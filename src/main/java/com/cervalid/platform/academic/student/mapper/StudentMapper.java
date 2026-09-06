package com.cervalid.platform.academic.student.mapper;

import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.student.dto.response.StudentDetailResponse;
import com.cervalid.platform.academic.student.dto.response.StudentResponse;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentResponse toResponse(
            Student student,
            User user,
            AcademicProfile profile) {

        StudentResponse response = new StudentResponse();

        response.setPublicId(student.getPublicId());
        response.setStudentCode(student.getStudentCode());
        response.setStatus(student.getStatus().name());
        response.setAdmissionDate(student.getAdmissionDate());
        response.setGraduationDate(student.getGraduationDate());

        // perfil academico
        response.setProgram(profile != null ?
                profile.getProgram() : null);

        response.setName(user.getName());
        response.setLastName(user.getLastName());

        response.setFullName(String.join(" ",
                        user.getName(),
                        user.getLastName()).trim());

        response.setEmail(user.getEmail());
        response.setDocument(user.getDocument());
        response.setPhone(user.getPhone());

        return response;
    }

    public StudentDetailResponse toDetail(
            Student student,
            User user) {

        StudentDetailResponse response =
                new StudentDetailResponse();

        response.setPublicId(student.getPublicId());
        response.setStudentCode(student.getStudentCode());
        response.setStatus(student.getStatus().name());
        response.setAdmissionDate(student.getAdmissionDate());
        response.setGraduationDate(student.getGraduationDate());

        response.setName(user.getName());
        response.setLastName(user.getLastName());

        response.setFullName(String.join(" ",
                        user.getName(),
                        user.getLastName()).trim());

        response.setEmail(user.getEmail());
        response.setDocument(user.getDocument());
        response.setPhone(user.getPhone());

        return response;
    }
}
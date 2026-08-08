package com.cervalid.platform.academic.profile.specification;

import com.cervalid.platform.academic.profile.dto.filter.AcademicProfileFilterRequest;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import org.springframework.data.jpa.domain.Specification;

public class AcademicProfileSpecification {

    private AcademicProfileSpecification() {
    }

    public static Specification<AcademicProfile> build(
            AcademicProfileFilterRequest filter,
            Long institutionId) {

        return Specification
                .where(byInstitution(institutionId))
                .and(byProgram(filter.getProgram()))
                .and(byFaculty(filter.getFaculty()))
                .and(byActive(filter.getActive()));
    }

    private static Specification<AcademicProfile> byInstitution(
            Long institutionId) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("institutionId"),
                        institutionId
                );
    }

    private static Specification<AcademicProfile> byProgram(
            String program) {

        if (program == null || program.isBlank()) {
            return null;
        }

        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("program")),
                        "%" + program.toLowerCase() + "%"
                );
    }

    private static Specification<AcademicProfile> byFaculty(
            String faculty) {

        if (faculty == null || faculty.isBlank()) {
            return null;
        }

        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("faculty")),
                        "%" + faculty.toLowerCase() + "%"
                );
    }

    private static Specification<AcademicProfile> byActive(
            Boolean active) {

        if (active == null) {
            return null;
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("active"),
                        active
                );
    }
}
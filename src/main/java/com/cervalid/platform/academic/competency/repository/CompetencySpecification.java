package com.cervalid.platform.academic.competency.repository;

import com.cervalid.platform.academic.competency.dto.filter.CompetencyFilterRequest;
import com.cervalid.platform.academic.competency.entity.Competency;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class CompetencySpecification {

    private CompetencySpecification() {
    }

    public static Specification<Competency> filter(
            CompetencyFilterRequest request,
            Long institutionId,
            Long studentId) {

        return (root, query, cb) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            predicates.add(
                    cb.equal(
                            root.get("institutionId"),
                            institutionId));

            if (studentId != null) {
                predicates.add(
                        cb.equal(
                                root.get("studentId"),
                                studentId));
            }

            if (request.getName() != null &&
                    !request.getName().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(
                                        root.get("name")),
                                "%" +
                                        request.getName()
                                                .trim()
                                                .toLowerCase() +
                                        "%"));
            }

            if (request.getLevel() != null) {

                predicates.add(
                        cb.equal(
                                root.get("level"),
                                request.getLevel()));
            }

            if (request.getStatus() != null) {

                predicates.add(
                        cb.equal(
                                root.get("status"),
                                request.getStatus()));
            }

            if (request.getAcademicPeriod() != null &&
                    !request.getAcademicPeriod().isBlank()) {

                predicates.add(
                        cb.equal(
                                root.get("academicPeriod"),
                                request.getAcademicPeriod()
                                        .trim()));
            }

            return cb.and(
                    predicates.toArray(
                            new Predicate[0]));
        };
    }
}
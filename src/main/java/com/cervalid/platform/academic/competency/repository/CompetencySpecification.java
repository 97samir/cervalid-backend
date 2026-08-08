package com.cervalid.platform.academic.competency.repository;

import com.cervalid.platform.academic.competency.dto.filter.CompetencyFilterRequest;
import com.cervalid.platform.academic.competency.entity.Competency;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CompetencySpecification {

    public static Specification<Competency> filter(
            CompetencyFilterRequest request,
            Long institutionId) {

        return (root, query, cb) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            predicates.add(
                    cb.equal(
                            root.get("institutionId"),
                            institutionId));

            if (request.getStudentPublicId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("studentPublicId"),
                                request.getStudentPublicId()));
            }

            if (request.getName() != null &&
                    !request.getName().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + request.getName().toLowerCase() + "%"));
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

            return cb.and(
                    predicates.toArray(new Predicate[0]));
        };
    }
}

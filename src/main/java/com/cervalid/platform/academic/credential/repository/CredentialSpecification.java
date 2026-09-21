package com.cervalid.platform.academic.credential.repository;

import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.dto.filter.CredentialFilterRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class CredentialSpecification {

    private CredentialSpecification() {
    }

    public static Specification<Credential> withFilters(
            Long institutionId,
            CredentialFilterRequest filter
    ) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    criteriaBuilder.equal(
                            root.get("institutionId"),
                            institutionId
                    )
            );

            predicates.add(
                    criteriaBuilder.isFalse(
                            root.get("deleted")
                    )
            );

            if (filter == null) {
                return criteriaBuilder.and(
                        predicates.toArray(new Predicate[0])
                );
            }

            if (filter.getStudentId() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("studentId"),
                                filter.getStudentId()
                        )
                );
            }

            if (filter.getType() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("type"),
                                filter.getType()
                        )
                );
            }

            if (filter.getStatus() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("status"),
                                filter.getStatus()
                        )
                );
            }

            if (filter.getCredentialNumber() != null
                    && !filter.getCredentialNumber().isBlank()) {

                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("credentialNumber")
                                ),
                                "%" + filter.getCredentialNumber()
                                        .trim()
                                        .toLowerCase() + "%"
                        )
                );
            }

            if (filter.getTitle() != null
                    && !filter.getTitle().isBlank()) {

                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("title")
                                ),
                                "%" + filter.getTitle()
                                        .trim()
                                        .toLowerCase() + "%"
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}
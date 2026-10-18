package com.cervalid.platform.academic.credential.repository;

import com.cervalid.platform.academic.credential.dto.filter.CredentialFilterRequest;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import com.cervalid.platform.academic.student.entity.Student;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class CredentialSpecification {

    private CredentialSpecification() {
    }

    public static Specification<Credential> withFilters(
            Long institutionId,
            CredentialFilterRequest filter,
            Long studentId
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(root.get("institutionId"), institutionId)
            );

            predicates.add(
                    cb.isFalse(root.get("deleted"))
            );

            if (studentId != null) {
                predicates.add(
                        cb.equal(root.get("studentId"), studentId)
                );
            }

            if (filter == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            if (filter.getType() != null) {
                predicates.add(
                        cb.equal(root.get("type"), filter.getType())
                );
            }

            if (filter.getStatus() != null) {
                predicates.add(
                        cb.equal(root.get("status"), filter.getStatus())
                );
            }

            addLikeFilter(
                    predicates, cb, root.get("credentialNumber"),
                    filter.getCredentialNumber()
            );

            addLikeFilter(
                    predicates, cb, root.get("title"),
                    filter.getTitle()
            );

            // Búsqueda general por número o título.
            if (hasText(filter.getSearch())) {
                String value = contains(filter.getSearch());

                predicates.add(
                        cb.or(
                                cb.like(
                                        cb.lower(root.get("credentialNumber")),
                                        value
                                ),
                                cb.like(
                                        cb.lower(root.get("title")),
                                        value
                                )
                        )
                );
            }

            // Filtrar por código del estudiante.
            if (hasText(filter.getStudentCode())) {

                Subquery<Long> studentQuery =
                        query.subquery(Long.class);

                var studentRoot = studentQuery.from(Student.class);

                studentQuery.select(studentRoot.get("id"));

                studentQuery.where(
                        cb.equal(
                                studentRoot.get("id"),
                                root.get("studentId")
                        ),
                        cb.equal(
                                studentRoot.get("institutionId"),
                                institutionId
                        ),
                        cb.isFalse(studentRoot.get("deleted")),
                        cb.like(
                                cb.lower(studentRoot.get("studentCode")),
                                contains(filter.getStudentCode())
                        )
                );

                predicates.add(cb.exists(studentQuery));
            }

            // Filtrar por programa académico.
            AcademicProgram program = filter.getProgram();

            if (program != null) {

                Subquery<Long> profileQuery =
                        query.subquery(Long.class);

                var profileRoot =
                        profileQuery.from(AcademicProfile.class);

                profileQuery.select(profileRoot.get("id"));

                profileQuery.where(
                        cb.equal(
                                profileRoot.get("studentId"),
                                root.get("studentId")
                        ),
                        cb.equal(
                                profileRoot.get("institutionId"),
                                institutionId
                        ),
                        cb.equal(
                                profileRoot.get("program"),
                                program
                        )
                );

                predicates.add(cb.exists(profileQuery));
            }

            // Rango inclusivo de fechas de emisión.
            if (filter.getFromDate() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("issuedAt"),
                                filter.getFromDate()
                        )
                );
            }

            if (filter.getToDate() != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("issuedAt"),
                                filter.getToDate()
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addLikeFilter(
            List<Predicate> predicates,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            jakarta.persistence.criteria.Path<String> path,
            String value
    ) {
        if (hasText(value)) {
            predicates.add(
                    cb.like(
                            cb.lower(path),
                            contains(value)
                    )
            );
        }
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static String contains(String value) {
        return "%" + value.trim().toLowerCase() + "%";
    }
}
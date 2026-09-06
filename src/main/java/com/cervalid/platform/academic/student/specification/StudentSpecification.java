package com.cervalid.platform.academic.student.specification;

import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.student.dto.filter.StudentFilterRequest;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.enums.StudentStatus;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {

    public static Specification<Student> build(
            StudentFilterRequest filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            predicates.add(
                    cb.isFalse(root.get("deleted"))
            );

            if (filter.getStudentCode() != null
                    && !filter.getStudentCode().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("studentCode")),
                                "%" +
                                        filter.getStudentCode()
                                                .toLowerCase()
                                        + "%"
                        )
                );
            }

            if (filter.getStatus() != null
                    && !filter.getStatus().isBlank()) {

                predicates.add(
                        cb.equal(
                                root.get("status"),
                                StudentStatus.valueOf(
                                        filter.getStatus()
                                                .toUpperCase()
                                )
                        )
                );
            }

            // traer program al filtro
            if (filter.getProgram() != null) {
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<AcademicProfile> profile =
                        subquery.from(AcademicProfile.class);

                subquery.select(profile.get("studentId"));

                subquery.where(
                        cb.equal(
                                profile.get("studentId"),
                                root.get("id")
                        ),
                        cb.equal(
                                profile.get("program"),
                                filter.getProgram()
                        )
                );

                predicates.add(
                        cb.exists(subquery)
                );
            }

            return cb.and(
                    predicates.toArray(
                            new Predicate[0]
                    )
            );
        };
    }
}
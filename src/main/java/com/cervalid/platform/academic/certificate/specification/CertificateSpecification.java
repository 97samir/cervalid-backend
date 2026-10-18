
package com.cervalid.platform.academic.certificate.specification;

import com.cervalid.platform.academic.certificate.dto.request.CertificateSearchRequest;
import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.student.entity.Student;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CertificateSpecification {

    private CertificateSpecification() {}

    public static Specification<Certificate> build(
            CertificateSearchRequest request,
            Long institutionId
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Institución activa y registros no eliminados.
            predicates.add(
                    cb.equal(root.get("institutionId"), institutionId)
            );

            predicates.add(
                    cb.isFalse(root.get("deleted"))
            );

            // Número de certificado.
            if (request.getCertificateNumber() != null
                    && !request.getCertificateNumber().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("certificateNumber")),
                                "%" + request.getCertificateNumber()
                                        .trim()
                                        .toLowerCase() + "%"
                        )
                );
            }

            // Estado.
            if (request.getStatus() != null) {
                predicates.add(
                        cb.equal(root.get("status"), request.getStatus())
                );
            }

            // Tipo de certificado.
            if (request.getType() != null) {
                predicates.add(
                        cb.equal(root.get("type"), request.getType())
                );
            }

            // Código de estudiante y/o identificador público.
            if ((request.getStudentCode() != null
                    && !request.getStudentCode().isBlank())
                    || request.getStudentPublicId() != null) {

                Subquery<Long> studentSubquery =
                        query.subquery(Long.class);

                Root<Student> studentRoot =
                        studentSubquery.from(Student.class);

                List<Predicate> studentPredicates =
                        new ArrayList<>();

                studentPredicates.add(
                        cb.equal(
                                studentRoot.get("institutionId"),
                                institutionId
                        )
                );

                studentPredicates.add(
                        cb.isFalse(studentRoot.get("deleted"))
                );

                if (request.getStudentCode() != null
                        && !request.getStudentCode().isBlank()) {

                    studentPredicates.add(
                            cb.like(
                                    cb.lower(studentRoot.get("studentCode")),
                                    "%" + request.getStudentCode()
                                            .trim()
                                            .toLowerCase() + "%"
                            )
                    );
                }

                if (request.getStudentPublicId() != null) {
                    studentPredicates.add(
                            cb.equal(
                                    studentRoot.get("publicId"),
                                    request.getStudentPublicId()
                            )
                    );
                }

                studentSubquery
                        .select(studentRoot.get("id"))
                        .where(
                                cb.and(
                                        studentPredicates.toArray(
                                                new Predicate[0]
                                        )
                                )
                        );

                predicates.add(
                        root.get("studentId").in(studentSubquery)
                );
            }

            // Programa académico o carrera.
            if (request.getProgram() != null) {

                Subquery<Long> profileSubquery =
                        query.subquery(Long.class);

                Root<AcademicProfile> profileRoot =
                        profileSubquery.from(AcademicProfile.class);

                profileSubquery
                        .select(profileRoot.get("studentId"))
                        .where(
                                cb.equal(
                                        profileRoot.get("institutionId"),
                                        institutionId
                                ),
                                cb.equal(
                                        profileRoot.get("program"),
                                        request.getProgram()
                                )
                        );

                predicates.add(
                        root.get("studentId").in(profileSubquery)
                );
            }

            // Fecha inicial de emisión, inclusive.
            if (request.getFromDate() != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("issuedAt"),
                                request.getFromDate().atStartOfDay()
                        )
                );
            }

            // Fecha final de emisión, inclusive.
            if (request.getToDate() != null) {
                predicates.add(
                        cb.lessThan(
                                root.get("issuedAt"),
                                request.getToDate()
                                        .plusDays(1)
                                        .atStartOfDay()
                        )
                );
            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}

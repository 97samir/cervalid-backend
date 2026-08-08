package com.cervalid.platform.academic.certificate.specification;

import com.cervalid.platform.academic.certificate.dto.request.CertificateSearchRequest;
import com.cervalid.platform.academic.certificate.entity.Certificate;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CertificateSpecification {

    private CertificateSpecification() {}

    public static Specification<Certificate> build(
            CertificateSearchRequest request,
            Long institutionId) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(
                            root.get("institutionId"),
                            institutionId
                    )
            );

            if (request.getCertificateNumber() != null &&
                    !request.getCertificateNumber().isBlank()) {

                predicates.add(
                        cb.like(
                                cb.lower(root.get("certificateNumber")),
                                "%" +
                                        request.getCertificateNumber()
                                                .toLowerCase()
                                        + "%"
                        )
                );
            }

            if (request.getStatus() != null) {

                predicates.add(
                        cb.equal(
                                root.get("status"),
                                request.getStatus()
                        )
                );
            }

            if (request.getType() != null) {

                predicates.add(
                        cb.equal(
                                root.get("type"),
                                request.getType()
                        )
                );
            }

            if (request.getIssuedFrom() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("issuedAt"),
                                request.getIssuedFrom()
                                        .atStartOfDay()
                        )
                );
            }

            if (request.getIssuedTo() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("issuedAt"),
                                request.getIssuedTo()
                                        .atTime(23,59,59)
                        )
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
package com.cervalid.platform.academic.verification.specification;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.verification.dto.request.VerificationHistoryFilterRequest;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class VerificationSpecification {

    private VerificationSpecification() {
    }

    public static Specification<VerificationRecord> filter(
            VerificationHistoryFilterRequest request,
            Long institutionId) {

        return (root, query, cb) -> {

            var predicate =
                    cb.equal(
                            root.get("institutionId"),
                            institutionId
                    );

            if (request == null) {
                return predicate;
            }

            if (request.getVerificationType() != null
                    && !request.getVerificationType().isBlank()) {

                String type =
                        request.getVerificationType()
                                .trim()
                                .toUpperCase();

                if ("CERTIFICATE".equals(type)) {

                    predicate = cb.and(
                            predicate,
                            cb.isNotNull(
                                    root.get("certificateId")
                            )
                    );
                }

                if ("CREDENTIAL".equals(type)) {

                    predicate = cb.and(
                            predicate,
                            cb.isNotNull(
                                    root.get("credentialId")
                            )
                    );
                }
            }

            if (request.getResourcePublicId() != null) {

                Subquery<Long> certificateSubquery =
                        query.subquery(Long.class);

                var certificateRoot =
                        certificateSubquery.from(
                                Certificate.class
                        );

                certificateSubquery.select(
                        certificateRoot.get("id")
                );

                certificateSubquery.where(
                        cb.and(
                                cb.equal(
                                        certificateRoot.get("publicId"),
                                        request.getResourcePublicId()
                                ),
                                cb.equal(
                                        certificateRoot.get("institutionId"),
                                        institutionId
                                )
                        )
                );

                Subquery<Long> credentialSubquery =
                        query.subquery(Long.class);

                var credentialRoot =
                        credentialSubquery.from(
                                Credential.class
                        );

                credentialSubquery.select(
                        credentialRoot.get("id")
                );

                credentialSubquery.where(
                        cb.and(
                                cb.equal(
                                        credentialRoot.get("publicId"),
                                        request.getResourcePublicId()
                                ),
                                cb.equal(
                                        credentialRoot.get("institutionId"),
                                        institutionId
                                ),
                                cb.isFalse(
                                        credentialRoot.get("deleted")
                                )
                        )
                );

                predicate = cb.and(
                        predicate,
                        cb.or(
                                cb.equal(
                                        root.get("certificateId"),
                                        certificateSubquery
                                ),
                                cb.equal(
                                        root.get("credentialId"),
                                        credentialSubquery
                                )
                        )
                );
            }

            if (request.getStatus() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("status"),
                                request.getStatus()
                        )
                );
            }

            if (request.getSearch() != null
                    && !request.getSearch().isBlank()) {

                String search =
                        "%"
                                + request.getSearch()
                                .trim()
                                .toLowerCase()
                                + "%";

                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(
                                        root.get("certificateNumber")
                                ),
                                search
                        )
                );
            }

            /* Fecha desde */
            if (request.getFromDate() != null) {

                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("verifiedAt"),
                                request.getFromDate()
                                        .atStartOfDay()
                        )
                );
            }

            /* Fecha hasta */
            if (request.getToDate() != null) {

                predicate = cb.and(
                        predicate,
                        cb.lessThanOrEqualTo(
                                root.get("verifiedAt"),
                                request.getToDate()
                                        .atTime(23, 59, 59)
                        )
                );
            }

            return predicate;
        };
    }
}
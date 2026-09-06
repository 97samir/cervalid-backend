package com.cervalid.platform.academic.verification.specification;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.verification.dto.request.VerificationCertificateSummaryFilterRequest;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class VerificationSummarySpecification {

    private VerificationSummarySpecification() {
    }

    public static Specification<Certificate> filter(
            VerificationCertificateSummaryFilterRequest request,
            Long institutionId) {

        return (root, query, cb) -> {

            var predicate = cb.equal(
                    root.get("institutionId"),
                    institutionId
            );

            if (request == null) {
                return predicate;
            }

            /*
             * BUSQUEDA
             */
            if (request.getSearch() != null
                    && !request.getSearch().isBlank()) {

                String search =
                        "%" +
                                request.getSearch()
                                        .trim()
                                        .toLowerCase() +
                                "%";

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

            /*
             * TIPO
             */
            if (request.getType() != null
                    && !request.getType().isBlank()) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("type"),
                                request.getType()
                        )
                );
            }

            /*
             * ESTADO DE LA ÚLTIMA VERIFICACIÓN
             */
            if (request.getStatus() != null) {

                Subquery<Long> latestStatusSubquery =
                        query.subquery(Long.class);

                Root<VerificationRecord> current =
                        latestStatusSubquery.from(
                                VerificationRecord.class
                        );

                latestStatusSubquery.select(
                        current.get("id")
                );

                /*
                 * Existe una verificación con el estado solicitado
                 */
                var conditions = cb.and(

                        cb.equal(
                                current.get("certificateId"),
                                root.get("id")
                        ),

                        cb.equal(
                                current.get("status"),
                                request.getStatus()
                        ),

                        /*
                         * Y no existe una verificación posterior.
                         */
                        cb.not(
                                cb.exists(
                                        createLaterVerificationSubquery(
                                                query,
                                                cb,
                                                root,
                                                current
                                        )
                                )
                        )
                );

                latestStatusSubquery.where(conditions);

                predicate = cb.and(
                        predicate,
                        cb.exists(latestStatusSubquery)
                );
            }

            /*
             * FECHA DESDE
             */
            if (request.getFromDate() != null) {

                LocalDateTime fromDate =
                        request.getFromDate()
                                .atStartOfDay();

                Subquery<Long> subquery =
                        query.subquery(Long.class);

                Root<VerificationRecord> verification =
                        subquery.from(
                                VerificationRecord.class
                        );

                subquery.select(
                        verification.get("id")
                );

                subquery.where(
                        cb.and(

                                cb.equal(
                                        verification.get("certificateId"),
                                        root.get("id")
                                ),

                                cb.greaterThanOrEqualTo(
                                        verification
                                                .<LocalDateTime>get("verifiedAt"),
                                        fromDate
                                )
                        )
                );

                predicate = cb.and(
                        predicate,
                        cb.exists(subquery)
                );
            }

            /*
             * FECHA HASTA
             */
            if (request.getToDate() != null) {

                LocalDateTime toDate =
                        request.getToDate()
                                .atTime(23, 59, 59);

                Subquery<Long> subquery =
                        query.subquery(Long.class);

                Root<VerificationRecord> verification =
                        subquery.from(
                                VerificationRecord.class
                        );

                subquery.select(
                        verification.get("id")
                );

                subquery.where(
                        cb.and(

                                cb.equal(
                                        verification.get("certificateId"),
                                        root.get("id")
                                ),

                                cb.lessThanOrEqualTo(
                                        verification
                                                .<LocalDateTime>get("verifiedAt"),
                                        toDate
                                )
                        )
                );

                predicate = cb.and(
                        predicate,
                        cb.exists(subquery)
                );
            }

            return predicate;
        };
    }

    private static Subquery<Long> createLaterVerificationSubquery(
            jakarta.persistence.criteria.CriteriaQuery<?> query,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            Root<Certificate> certificateRoot,
            Root<VerificationRecord> currentVerification) {

        Subquery<Long> subquery =
                query.subquery(Long.class);

        Root<VerificationRecord> later =
                subquery.from(
                        VerificationRecord.class
                );

        subquery.select(
                later.get("id")
        );

        subquery.where(
                cb.and(

                        /*
                         * Mismo certificado
                         */
                        cb.equal(
                                later.get("certificateId"),
                                certificateRoot.get("id")
                        ),

                        /*
                         * Fecha posterior
                         */
                        cb.greaterThan(
                                later.<LocalDateTime>get("verifiedAt"),
                                currentVerification
                                        .<LocalDateTime>get("verifiedAt")
                        )
                )
        );

        return subquery;
    }
}
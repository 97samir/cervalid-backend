package com.cervalid.platform.academic.verification.specification;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.verification.dto.request.VerificationSummaryFilterRequest;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class VerificationSummarySpecification {

    private VerificationSummarySpecification() {
    }

    /*
     * =========================================================
     * CERTIFICATE
     * =========================================================
     */

    public static Specification<Certificate> filterCertificates(
            VerificationSummaryFilterRequest request,
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

            /*
             * BÚSQUEDA
             */
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
             * ÚLTIMO ESTADO DE VERIFICACIÓN
             */
            if (request.getStatus() != null) {

                predicate = cb.and(
                        predicate,
                        buildLatestStatusPredicateForCertificate(
                                root,
                                query,
                                cb,
                                request
                        )
                );
            }

            /*
             * FECHA DESDE
             */
            if (request.getFromDate() != null) {

                LocalDateTime fromDate =
                        request.getFromDate()
                                .atStartOfDay();

                predicate = cb.and(
                        predicate,
                        buildDateExistsPredicateForCertificate(
                                root,
                                query,
                                cb,
                                fromDate,
                                null
                        )
                );
            }

            /*
             * FECHA HASTA
             */
            if (request.getToDate() != null) {

                LocalDateTime toDate =
                        request.getToDate()
                                .atTime(23, 59, 59);

                predicate = cb.and(
                        predicate,
                        buildDateExistsPredicateForCertificate(
                                root,
                                query,
                                cb,
                                null,
                                toDate
                        )
                );
            }

            return predicate;
        };
    }

    /*
     * =========================================================
     * CREDENTIAL
     * =========================================================
     */

    public static Specification<Credential> filterCredentials(
            VerificationSummaryFilterRequest request,
            Long institutionId) {

        return (root, query, cb) -> {

            var predicate =
                    cb.and(
                            cb.equal(
                                    root.get("institutionId"),
                                    institutionId
                            ),
                            cb.isFalse(
                                    root.get("deleted")
                            )
                    );

            if (request == null) {
                return predicate;
            }

            /*
             * BÚSQUEDA
             */
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
                                        root.get("credentialNumber")
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
             * ÚLTIMO ESTADO DE VERIFICACIÓN
             */
            if (request.getStatus() != null) {

                predicate = cb.and(
                        predicate,
                        buildLatestStatusPredicateForCredential(
                                root,
                                query,
                                cb,
                                request
                        )
                );
            }

            /*
             * FECHA DESDE
             */
            if (request.getFromDate() != null) {

                LocalDateTime fromDate =
                        request.getFromDate()
                                .atStartOfDay();

                predicate = cb.and(
                        predicate,
                        buildDateExistsPredicateForCredential(
                                root,
                                query,
                                cb,
                                fromDate,
                                null
                        )
                );
            }

            /*
             * FECHA HASTA
             */
            if (request.getToDate() != null) {

                LocalDateTime toDate =
                        request.getToDate()
                                .atTime(23, 59, 59);

                predicate = cb.and(
                        predicate,
                        buildDateExistsPredicateForCredential(
                                root,
                                query,
                                cb,
                                null,
                                toDate
                        )
                );
            }

            return predicate;
        };
    }

    /*
     * =========================================================
     * CERTIFICATE - ÚLTIMO STATUS
     * =========================================================
     */

    private static jakarta.persistence.criteria.Predicate
    buildLatestStatusPredicateForCertificate(
            Root<Certificate> certificateRoot,
            jakarta.persistence.criteria.CriteriaQuery<?> query,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            VerificationSummaryFilterRequest request) {

        Subquery<Long> latestStatusSubquery =
                query.subquery(Long.class);

        Root<VerificationRecord> current =
                latestStatusSubquery.from(
                        VerificationRecord.class
                );

        latestStatusSubquery.select(
                current.get("id")
        );

        latestStatusSubquery.where(
                cb.and(

                        cb.equal(
                                current.get("certificateId"),
                                certificateRoot.get("id")
                        ),

                        cb.equal(
                                current.get("status"),
                                request.getStatus()
                        ),

                        cb.not(
                                cb.exists(
                                        createLaterCertificateVerificationSubquery(
                                                query,
                                                cb,
                                                certificateRoot,
                                                current
                                        )
                                )
                        )
                )
        );

        return cb.exists(
                latestStatusSubquery
        );
    }

    /*
     * =========================================================
     * CREDENTIAL - ÚLTIMO STATUS
     * =========================================================
     */

    private static jakarta.persistence.criteria.Predicate
    buildLatestStatusPredicateForCredential(
            Root<Credential> credentialRoot,
            jakarta.persistence.criteria.CriteriaQuery<?> query,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            VerificationSummaryFilterRequest request) {

        Subquery<Long> latestStatusSubquery =
                query.subquery(Long.class);

        Root<VerificationRecord> current =
                latestStatusSubquery.from(
                        VerificationRecord.class
                );

        latestStatusSubquery.select(
                current.get("id")
        );

        latestStatusSubquery.where(
                cb.and(

                        cb.equal(
                                current.get("credentialId"),
                                credentialRoot.get("id")
                        ),

                        cb.equal(
                                current.get("status"),
                                request.getStatus()
                        ),

                        cb.not(
                                cb.exists(
                                        createLaterCredentialVerificationSubquery(
                                                query,
                                                cb,
                                                credentialRoot,
                                                current
                                        )
                                )
                        )
                )
        );

        return cb.exists(
                latestStatusSubquery
        );
    }

    /*
     * =========================================================
     * CERTIFICATE - FECHA
     * =========================================================
     */

    private static jakarta.persistence.criteria.Predicate
    buildDateExistsPredicateForCertificate(
            Root<Certificate> certificateRoot,
            jakarta.persistence.criteria.CriteriaQuery<?> query,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            LocalDateTime fromDate,
            LocalDateTime toDate) {

        Subquery<Long> subquery =
                query.subquery(Long.class);

        Root<VerificationRecord> verification =
                subquery.from(
                        VerificationRecord.class
                );

        subquery.select(
                verification.get("id")
        );

        var conditions =
                cb.equal(
                        verification.get("certificateId"),
                        certificateRoot.get("id")
                );

        if (fromDate != null) {

            conditions = cb.and(
                    conditions,
                    cb.greaterThanOrEqualTo(
                            verification.<LocalDateTime>get(
                                    "verifiedAt"
                            ),
                            fromDate
                    )
            );
        }

        if (toDate != null) {

            conditions = cb.and(
                    conditions,
                    cb.lessThanOrEqualTo(
                            verification.<LocalDateTime>get(
                                    "verifiedAt"
                            ),
                            toDate
                    )
            );
        }

        subquery.where(conditions);

        return cb.exists(subquery);
    }

    /*
     * =========================================================
     * CREDENTIAL - FECHA
     * =========================================================
     */

    private static jakarta.persistence.criteria.Predicate
    buildDateExistsPredicateForCredential(
            Root<Credential> credentialRoot,
            jakarta.persistence.criteria.CriteriaQuery<?> query,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            LocalDateTime fromDate,
            LocalDateTime toDate) {

        Subquery<Long> subquery =
                query.subquery(Long.class);

        Root<VerificationRecord> verification =
                subquery.from(
                        VerificationRecord.class
                );

        subquery.select(
                verification.get("id")
        );

        var conditions =
                cb.equal(
                        verification.get("credentialId"),
                        credentialRoot.get("id")
                );

        if (fromDate != null) {

            conditions = cb.and(
                    conditions,
                    cb.greaterThanOrEqualTo(
                            verification.<LocalDateTime>get(
                                    "verifiedAt"
                            ),
                            fromDate
                    )
            );
        }

        if (toDate != null) {

            conditions = cb.and(
                    conditions,
                    cb.lessThanOrEqualTo(
                            verification.<LocalDateTime>get(
                                    "verifiedAt"
                            ),
                            toDate
                    )
            );
        }

        subquery.where(conditions);

        return cb.exists(subquery);
    }

    /*
     * =========================================================
     * LATEST CERTIFICATE VERIFICATION
     * =========================================================
     */

    private static Subquery<Long>
    createLaterCertificateVerificationSubquery(
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

                        cb.equal(
                                later.get("certificateId"),
                                certificateRoot.get("id")
                        ),

                        cb.greaterThan(
                                later.<LocalDateTime>get(
                                        "verifiedAt"
                                ),
                                currentVerification
                                        .<LocalDateTime>get(
                                                "verifiedAt"
                                        )
                        )
                )
        );

        return subquery;
    }

    /*
     * =========================================================
     * LATEST CREDENTIAL VERIFICATION
     * =========================================================
     */

    private static Subquery<Long>
    createLaterCredentialVerificationSubquery(
            jakarta.persistence.criteria.CriteriaQuery<?> query,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            Root<Credential> credentialRoot,
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

                        cb.equal(
                                later.get("credentialId"),
                                credentialRoot.get("id")
                        ),

                        cb.greaterThan(
                                later.<LocalDateTime>get(
                                        "verifiedAt"
                                ),
                                currentVerification
                                        .<LocalDateTime>get(
                                                "verifiedAt"
                                        )
                        )
                )
        );

        return subquery;
    }
}
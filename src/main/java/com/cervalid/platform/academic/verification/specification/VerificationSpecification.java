package com.cervalid.platform.academic.verification.specification;

import com.cervalid.platform.academic.verification.dto.request.VerificationHistoryFilterRequest;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import org.springframework.data.jpa.domain.Specification;

public class VerificationSpecification {

    private VerificationSpecification() {
    }

    public static Specification<VerificationRecord> filter(
            VerificationHistoryFilterRequest request,
            Long institutionId,
            Long certificateId) {

        return (root, query, cb) -> {

            var predicate =
                    cb.equal(
                            root.get("institutionId"),
                            institutionId);

            if(request == null){
                return predicate;
            }

            // Certificado
            if (certificateId != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("certificateId"),
                                certificateId));
            }

            // estado
            if (request.getStatus() != null) {
                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("status"),
                                request.getStatus()));
            }
            // numero de cerrtificado
            if (request.getSearch() != null &&
                    !request.getSearch().isBlank()) {

                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(root.get("certificateNumber")),
                                "%" + request.getSearch()
                                        .trim()
                                        .toLowerCase() + "%"));
            }

            // fecha desde
            if (request.getFromDate() != null) {

                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("verifiedAt"),
                                request.getFromDate()
                                        .atStartOfDay()));
            }

            // fecha hasta
            if (request.getToDate() != null) {

                predicate = cb.and(
                        predicate,
                        cb.lessThanOrEqualTo(
                                root.get("verifiedAt"),
                                request.getToDate()
                                        .atTime(23,59,59)));
            }

            return predicate;
        };
    }

}
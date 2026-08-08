package com.cervalid.platform.academic.timeline.repository;

import com.cervalid.platform.academic.timeline.dto.request.TimelineFilterRequest;
import com.cervalid.platform.academic.timeline.entity.TimelineEvent;
import org.springframework.data.jpa.domain.Specification;

public class TimelineSpecification {

    private TimelineSpecification() {}

    public static Specification<TimelineEvent> filter(
            TimelineFilterRequest request,
            Long institutionId,
            Long studentId) {

        return (root,
                query,
                cb) -> {

            var predicate =
                    cb.equal(
                            root.get("institutionId"),
                            institutionId);

            if (studentId  != null) {

                predicate = cb.and(predicate,
                                cb.equal(
                                        root.get("studentId"),
                                        studentId ));
            }

            if (request.getType() != null) {

                predicate = cb.and(predicate,
                                cb.equal(
                                        root.get("type"),
                                        request.getType()));
            }

            if (request.getSource() != null) {

                predicate = cb.and(predicate,
                                cb.equal(
                                        root.get("source"),
                                        request.getSource()));
            }

            if (request.getFromDate() != null) {

                predicate = cb.and(predicate,
                                cb.greaterThanOrEqualTo(
                                        root.get("eventDate"),
                                        request.getFromDate()
                                                .atStartOfDay()));
            }

            if (request.getToDate() != null) {

                predicate = cb.and(predicate,
                                cb.lessThanOrEqualTo(
                                        root.get("eventDate"),
                                        request.getToDate()
                                                .atTime(23,59,59)));
            }

            if (request.getKeyword() != null
                    && !request.getKeyword().isBlank()) {

                String likeValue =
                        "%" +
                                request.getKeyword()
                                        .trim()
                                        .toLowerCase()
                                + "%";

                predicate = cb.and(
                        predicate,

                        cb.or(

                                cb.like(
                                        cb.lower(root.get("title")),
                                        likeValue),

                                cb.like(
                                        cb.lower(root.get("description")),
                                        likeValue)

                        )
                );
            }

            if (request.getReferenceType() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("referenceType"),
                                request.getReferenceType()
                        )
                );
            }

            return predicate;

        };
    }
}
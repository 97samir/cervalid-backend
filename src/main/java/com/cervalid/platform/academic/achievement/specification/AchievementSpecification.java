package com.cervalid.platform.academic.achievement.specification;

import com.cervalid.platform.academic.achievement.entity.Achievement;
import com.cervalid.platform.academic.achievement.enums.AchievementStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AchievementSpecification {

    public static Specification<Achievement> filter(
            Long institutionId,
            Long studentId,
            String title,
            String type,
            String status,
            String academicPeriod
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(root.get("institutionId"), institutionId)
            );

            if (title != null && !title.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + title.toLowerCase() + "%"
                        )
                );
            }

            if (type != null && !type.isBlank()) {
                predicates.add(
                        cb.equal(root.get("type"), type)
                );
            }

            if (status != null && !status.isBlank()) {
                predicates.add(
                        cb.equal(
                                root.get("status"),
                                AchievementStatus.valueOf(status)
                        )
                );
            }

            if (studentId != null) {
                predicates.add(
                        cb.equal(root.get("studentId"), studentId)
                );
            }

            if (academicPeriod != null
                    && !academicPeriod.isBlank()) {

                predicates.add(
                        cb.equal(
                                root.get("academicPeriod"),
                                academicPeriod
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
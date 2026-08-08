package com.cervalid.platform.academic.achievement.repository;

import com.cervalid.platform.academic.achievement.entity.Achievement;
import com.cervalid.platform.academic.achievement.enums.AchievementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.*;

public interface AchievementRepository
        extends JpaRepository<Achievement, Long>,
        JpaSpecificationExecutor<Achievement> {

    //Optional<Achievement> findByPublicId(
    //        UUID publicId);

    Optional<Achievement> findByPublicIdAndInstitutionId(
            UUID publicId,
            Long institutionId
    );

    List<Achievement> findByStudentId(
            Long studentId);

    boolean existsByStudentIdAndTitle(
            Long studentId,
            String title);

    List<Achievement> findByStudentIdAndStatus(
            Long studentId,
            AchievementStatus status);

    Page<Achievement> findByInstitutionId(
            Long institutionId,
            Pageable pageable);
}
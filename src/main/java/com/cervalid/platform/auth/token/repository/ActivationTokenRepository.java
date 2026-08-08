package com.cervalid.platform.auth.token.repository;

import com.cervalid.platform.auth.token.entity.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Long> {

    Optional<ActivationToken> findByToken(String token);

    List<ActivationToken> findByInstitution_Id(Long institutionId);

    List<ActivationToken> findAllByUser_IdAndInstitution_IdAndUsedFalse(
            Long userId,
            Long institutionId
    );

    Optional<ActivationToken> findTopByUserIdAndInstitutionIdOrderByCreatedAtDesc(
            Long userId,
            Long institutionId
    );

    boolean existsByUser_IdAndInstitution_IdAndUsedFalse(
            Long userId,
            Long institutionId
    );

}

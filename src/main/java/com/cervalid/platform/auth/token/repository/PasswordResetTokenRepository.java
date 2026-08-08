package com.cervalid.platform.auth.token.repository;

import com.cervalid.platform.auth.token.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken t WHERE t.user.id = :userId")
    void deleteByUserId(Long userId);
    Optional<PasswordResetToken> findByToken(String token);
}

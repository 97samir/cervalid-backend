package com.cervalid.platform.auth.repository;

import com.cervalid.platform.auth.entity.UserRefreshToken;
import com.cervalid.platform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRefreshTokenRepository
        extends JpaRepository<UserRefreshToken, Long> {

    Optional<UserRefreshToken> findByTokenAndRevokedFalse(String token);
    void deleteByUser(User user); // borrar tokens viejos
}
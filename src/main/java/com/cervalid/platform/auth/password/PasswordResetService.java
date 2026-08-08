package com.cervalid.platform.auth.password;

import com.cervalid.platform.auth.token.entity.PasswordResetToken;
import com.cervalid.platform.auth.token.repository.PasswordResetTokenRepository;
import com.cervalid.platform.notification.email.EmailService;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Transactional
    public void requestReset(String email) {

        userRepository.findByEmail(email).ifPresent(user -> {

            // invalidar token previo (si existe)
            tokenRepository.deleteByUserId(user.getId());
            // crear nuevo token
            PasswordResetToken token = new PasswordResetToken();
            token.setToken(UUID.randomUUID().toString());
            token.setUser(user);
            token.setExpiresAt(LocalDateTime.now().plusMinutes(30));
            token.setUsed(false);

            tokenRepository.save(token);

            // Enviar correo
            String link = frontendUrl + "/reset-password?token=" + token.getToken();
            emailService.sendActivationEmail(user.getEmail(), link);
        }); // Siempre OK, sin filtrar existencia
    }

    @Transactional
    public void confirmReset(String tokenValue, String newPassword) {

        PasswordResetToken token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (token.isUsed() || token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        token.setUsed(true);

        userRepository.save(user);
        tokenRepository.save(token);
    }
}

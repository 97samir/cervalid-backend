package com.cervalid.platform.invitation.service;
// notifica una invitación
import com.cervalid.platform.notification.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationEmailService {

    private final EmailService emailService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void inviteUser(String email, String token, boolean isResend) {

        String link = frontendUrl + "/activate?token=" + token;

        if (isResend) {
            log.info("RE-SENDING ACTIVATION LINK");
        }

        log.info("EMAIL: {}", email);
        log.info("ACTIVACIÓN LINK:{} ", link);

        try {

            emailService.sendActivationEmail(email, link);

        } catch (Exception e) {

            log.error("Error enviando correo",e);
        }

    }
}

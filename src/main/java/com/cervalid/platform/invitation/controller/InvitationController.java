package com.cervalid.platform.invitation.controller;

import com.cervalid.platform.auth.token.entity.ActivationToken;
import com.cervalid.platform.invitation.dto.InvitationRequest;
import com.cervalid.platform.invitation.dto.InvitationResponse;
import com.cervalid.platform.invitation.service.InvitationEmailService;
import com.cervalid.platform.invitation.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invitations")
public class InvitationController {

    private final InvitationService invitationService;
    private final InvitationEmailService invitationEmailService;

    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping
    public ResponseEntity<?> inviteUser(
            @RequestBody InvitationRequest request) {

        ActivationToken token = invitationService.inviteUser(request);
        // envio de email
        invitationEmailService.inviteUser(
                request.getEmail(),
                token.getToken(),
                false
        );

        return ResponseEntity.ok("Invitación enviada correctamente");
    }

    @GetMapping
    public List<InvitationResponse> getInvitations() {
        return invitationService.getInvitations();
    }

    // reenviar invitación
    @PostMapping("/resend")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public ResponseEntity<?> resendInvitation(@RequestParam String email) {

        invitationService.resendInvitation(email);

        return ResponseEntity.ok("Invitación reenviada correctamente");
    }
}

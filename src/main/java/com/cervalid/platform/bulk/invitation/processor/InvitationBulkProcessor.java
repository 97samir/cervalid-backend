package com.cervalid.platform.bulk.invitation.processor;

import com.cervalid.platform.auth.token.dto.ActivationTokenRequest;
import com.cervalid.platform.auth.token.entity.ActivationToken;
import com.cervalid.platform.auth.token.service.ActivationTokenService;
import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRowRequest;
import com.cervalid.platform.bulk.invitation.dto.response.InvitationBulkExecutionItemResponse;
import com.cervalid.platform.bulk.invitation.mapper.InvitationMembershipMapper;
import com.cervalid.platform.invitation.service.InvitationEmailService;
import com.cervalid.platform.membership.service.InstitutionMembershipService;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.provisioning.dto.UserProvisioningRequest;
import com.cervalid.platform.user.provisioning.service.UserProvisioningService;
import com.cervalid.platform.security.context.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvitationBulkProcessor {

    private final UserProvisioningService userProvisioningService;
    private final InstitutionMembershipService membershipService;
    private final InvitationMembershipMapper membershipMapper;
    private final ActivationTokenService tokenService;
    private final InvitationEmailService invitationEmailService;

    public InvitationBulkExecutionItemResponse process(
            InvitationBulkRowRequest row,
            Long institutionId,
            Integer rowNumber) {

        List<String> errors = new ArrayList<>();


        try {
            // USER PROVISIONING
            User user;
            try {
                user = userProvisioningService.provisionUser(
                        UserProvisioningRequest.builder()
                                .email(row.getEmail())
                                .build()
                );
            } catch (Exception e) {
                errors.add("USER: " + e.getMessage());
                return buildFailure(row, rowNumber, errors);
            }

            // MEMBERSHIP CREATION
            try {
                membershipService.createMembership(
                        membershipMapper.toMembershipRequest(
                                row,
                                user.getId(),
                                institutionId
                        )
                );
            } catch (Exception e) {
                errors.add("MEMBERSHIP: " + e.getMessage());
                return buildFailure(row, rowNumber, errors);
            }

            if (tokenService.hasActiveToken(user.getId(), institutionId)) {
                return buildFailure(
                        row,
                        rowNumber,
                        List.of("USER_ALREADY_INVITED")
                );
            }
            // TOKEN CREATION
            ActivationToken token;
            try {
                token = tokenService.getOrCreateToken(
                        user.getId(),
                        institutionId,
                        UserContext.getUserId()
                );
            } catch (Exception e) {
                errors.add("TOKEN: " + e.getMessage());
                return buildFailure(row, rowNumber, errors);
            }

            // EMAIL (NO CRITICAL)
            try {
                boolean isResend = token.getCreatedAt()
                        .isBefore(LocalDateTime.now().minusMinutes(10));

                invitationEmailService.inviteUser(
                        user.getEmail(),
                        token.getToken(),
                        isResend
                );

            } catch (Exception e) {
                errors.add("EMAIL: " + e.getMessage());
                // NO rompemos el flujo por email
            }

            // SUCCESS
            return InvitationBulkExecutionItemResponse.builder()
                    .rowNumber(rowNumber)
                    .email(user.getEmail())
                    .success(true)
                    .message("Invitación procesada correctamente")
                    .errors(errors)
                    .build();

        } catch (Exception e) {
            errors.add("UNEXPECTED: " + e.getMessage());
            return buildFailure(row, rowNumber, errors);
        }
    }

    // CENTRALIZED FAILURE BUILDER
    private InvitationBulkExecutionItemResponse buildFailure(
            InvitationBulkRowRequest row,
            Integer rowNumber,
            List<String> errors) {

        return InvitationBulkExecutionItemResponse.builder()
                .rowNumber(rowNumber)
                .email(row.getEmail())
                .success(false)
                .message("Error en procesamiento de invitación")
                .errors(errors)
                .build();
    }
}
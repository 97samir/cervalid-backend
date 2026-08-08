package com.cervalid.platform.bulk.validation.business;

import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRowRequest;
import com.cervalid.platform.bulk.validation.business.validators.AllowedRoleValidator;
import com.cervalid.platform.bulk.validation.business.validators.DuplicateEmailValidator;
import com.cervalid.platform.bulk.validation.business.validators.ExistingMembershipValidator;
import com.cervalid.platform.bulk.validation.business.validators.ExistingPendingInvitationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvitationBusinessValidator {

    private final DuplicateEmailValidator duplicateEmailValidator;
    private final ExistingMembershipValidator existingMembershipValidator;
    private final ExistingPendingInvitationValidator existingPendingInvitationValidator;

    private final AllowedRoleValidator allowedRoleValidator;

    public List<String> validate(
            List<InvitationBulkRowRequest> rows,
            InvitationBulkRowRequest currentRow,
            Long institutionId) {

        List<String> errors = new ArrayList<>();

        duplicateEmailValidator.validate(
                rows,
                currentRow,
                errors
        );

        validateMembership(currentRow, institutionId, errors);
        validatePendingInvitation(currentRow, institutionId, errors);
        validateRole(currentRow, errors);

        return errors;
    }

    private void validateMembership(
            InvitationBulkRowRequest row,
            Long institutionId,
            List<String> errors) {

        String error =
                existingMembershipValidator
                        .validate(
                                row.getEmail(),
                                institutionId);

        if (error != null) {
            errors.add(error);
        }
    }

    private void validatePendingInvitation(
            InvitationBulkRowRequest row,
            Long institutionId,
            List<String> errors) {

        String error =
                existingPendingInvitationValidator
                        .validate(
                                row.getEmail(),
                                institutionId);

        if (error != null) {
            errors.add(error);
        }
    }

    private void validateRole(
            InvitationBulkRowRequest row,
            List<String> errors) {

        String error =
                allowedRoleValidator
                        .validate(row.getRole());

        if (error != null) {
            errors.add(error);
        }
    }
}

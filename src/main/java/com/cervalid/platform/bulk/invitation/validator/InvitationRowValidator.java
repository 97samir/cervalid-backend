package com.cervalid.platform.bulk.invitation.validator;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRowRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InvitationRowValidator {

    public List<String> validate(InvitationBulkRowRequest row) {

        List<String> errors = new ArrayList<>();
        validateEmail(row, errors);
        validateRole(row, errors);
        return errors;
    }

    private void validateEmail(
            InvitationBulkRowRequest row,
            List<String> errors) {

        if (row.getEmail() == null || row.getEmail().isBlank()) {
            errors.add("Email requerido");
            return;
        }

        if (!row.getEmail().matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )) {
            errors.add("Email inválido");
        }
    }

    private void validateRole(
            InvitationBulkRowRequest row,
            List<String> errors
    ) {

        if (row.getRole() == null || row.getRole().isBlank()) {
            errors.add("Role requerido");
            return;
        }

        try {

            RoleName.valueOf(row.getRole());

        } catch (Exception e) {

            errors.add("Rol inválido");
        }
    }
}

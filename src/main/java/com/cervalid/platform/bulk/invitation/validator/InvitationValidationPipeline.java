package com.cervalid.platform.bulk.invitation.validator;

import com.cervalid.platform.bulk.common.dto.ValidationResult;
import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRowRequest;
import com.cervalid.platform.bulk.validation.business.InvitationBusinessValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvitationValidationPipeline {

    private final InvitationRowValidator structuralValidator;
    private final InvitationBusinessValidator businessValidator;

    public ValidationResult validate(
            List<InvitationBulkRowRequest> rows,
            InvitationBulkRowRequest currentRow,
            Long institutionId) {

        List<String> errors = new ArrayList<>();

        // STRUCTURAL VALIDATION
        errors.addAll(structuralValidator.validate(currentRow));

        // BUSINESS VALIDATION
        errors.addAll(businessValidator.validate(
                rows, currentRow, institutionId));

        return ValidationResult.builder()
                .valid(errors.isEmpty())
                .errors(errors)
                .build();
    }
}
package com.cervalid.platform.bulk.validation.business.validators;

import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRowRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DuplicateEmailValidator {

    public void validate(
            List<InvitationBulkRowRequest> rows,
            InvitationBulkRowRequest currentRow,
            List<String> errors) {

        Map<String, Long> emailCount =
                rows.stream()
                        .collect(Collectors.groupingBy(
                                InvitationBulkRowRequest::getEmail,
                                Collectors.counting()
                        ));

        Long count =
                emailCount.get(currentRow.getEmail());

        if (count != null && count > 1) {
            errors.add("Email duplicado en archivo");
        }
    }
}
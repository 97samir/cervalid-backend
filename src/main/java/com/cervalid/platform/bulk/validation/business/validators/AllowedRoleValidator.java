package com.cervalid.platform.bulk.validation.business.validators;

import com.cervalid.platform.common.enums.RoleName;
import org.springframework.stereotype.Component;

@Component
public class AllowedRoleValidator {

    public String validate(String role) {

        try {
            RoleName roleName =
                    RoleName.valueOf(role);
            if (roleName == RoleName.SUPER_ADMIN) {
                return "SUPER_ADMIN no permitido";
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}

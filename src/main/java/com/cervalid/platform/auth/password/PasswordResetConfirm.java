package com.cervalid.platform.auth.password;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetConfirm {
    private String token;
    private String newPassword;
}

package com.cervalid.platform.auth.password;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService service;

    @PostMapping("/request")
    public void request(@RequestBody PasswordResetRequest request) throws Exception {
        service.requestReset(request.getEmail());
    }

    @PostMapping("/confirm")
    public void confirm(@RequestBody PasswordResetConfirm request) {
        service.confirmReset(request.getToken(), request.getNewPassword());
    }
}

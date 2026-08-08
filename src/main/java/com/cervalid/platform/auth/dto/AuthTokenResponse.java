package com.cervalid.platform.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor

public class AuthTokenResponse {

    private String token;
    private String refreshToken;
}

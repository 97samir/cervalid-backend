package com.cervalid.platform.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenRequest {

    private String refreshToken;
}

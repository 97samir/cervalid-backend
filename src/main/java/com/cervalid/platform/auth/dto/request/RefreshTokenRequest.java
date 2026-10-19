package com.cervalid.platform.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenRequest {

    private String refreshToken;
}

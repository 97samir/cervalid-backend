package com.cervalid.platform.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {

    private String token;
    private String refreshToken;

    private Long userId;
    private List<InstitutionOption> institutions;

    private String selectionToken;

    public static LoginResponse superAdmin(
            String token,
            String refreshToken
    ){
        return authenticated(token, refreshToken);
    }

    public static LoginResponse singleInstitution(
            String token,
            String refreshToken
    ) {
        return authenticated(token, refreshToken);
    }

    public static LoginResponse multiInstitution(
            Long userId,
            List<InstitutionOption> institutions,
            String selectionToken
    ){
        LoginResponse response = new LoginResponse();
        response.setUserId(userId);
        response.setInstitutions(institutions);
        response.setSelectionToken(selectionToken);
        return response;
    }

    private static LoginResponse authenticated(
            String token,
            String refreshToken
    ) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        return response;
    }
}
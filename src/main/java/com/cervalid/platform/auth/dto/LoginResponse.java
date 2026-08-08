package com.cervalid.platform.auth.dto;

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

    public static LoginResponse superAdmin(String token, String refreshToken){
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        return response;
    }

    public static LoginResponse singleInstitution(String token, String refreshToken){
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        return response;
    }

    public static LoginResponse multiInstitution(Long userId, List<InstitutionOption> institutions){
        LoginResponse response = new LoginResponse();
        response.setUserId(userId);
        response.setInstitutions(institutions);
        return response;
    }
}
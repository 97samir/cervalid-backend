package com.cervalid.platform.auth.controller;

import com.cervalid.platform.auth.dto.response.AuthTokenResponse;
import com.cervalid.platform.auth.dto.request.LoginRequest;
import com.cervalid.platform.auth.dto.response.LoginResponse;
import com.cervalid.platform.auth.dto.response.MeResponse;
import com.cervalid.platform.auth.dto.request.RefreshTokenRequest;
import com.cervalid.platform.auth.dto.request.SelectInstitutionRequest;
import com.cervalid.platform.auth.service.AuthQueryService;
import com.cervalid.platform.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String ACCESS_COOKIE = "cervalid_token";
    private static final String REFRESH_COOKIE = "cervalid_refreshToken";

    private static final long ACCESS_COOKIE_MAX_AGE = 60 * 60;
    private static final long REFRESH_COOKIE_MAX_AGE = 7 * 24 * 60 * 60;

    private static final String SELECTION_COOKIE = "cervalid_selection";
    private static final long SELECTION_COOKIE_MAX_AGE = 5 * 60;

    private final AuthService authService;
    private final AuthQueryService authQueryService;

    @Value("${app.cookie.secure:false}")
    private boolean secureCookies;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        LoginResponse loginResponse = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        String selectionToken = loginResponse.getSelectionToken();

        if (loginResponse.getToken() != null) {
            // Login de superadministrador o usuario con una institución.
            addAuthCookies(
                    response,
                    loginResponse.getToken(),
                    loginResponse.getRefreshToken()
            );
        } else {
            // Login con varias instituciones: todavía no hay sesión normal.
            clearCookie(response, "cervalid_token");
            clearCookie(response, "cervalid_refreshToken");
        }

        if (selectionToken != null && !selectionToken.isBlank()) {
            // Desafío temporal, válido durante cinco minutos.
            addCookie(
                    response,
                    "cervalid_selection",
                    selectionToken,
                    5 * 60
            );
        } else {
            // Evita conservar un desafío de una sesión anterior.
            clearCookie(response, "cervalid_selection");
        }

        // El desafío se transmite únicamente mediante la cookie HttpOnly.
        loginResponse.setSelectionToken(null);

        return ResponseEntity.ok(loginResponse);
    }

    // SELECT INSTITUTION
    @PostMapping("/select-institution")
    public ResponseEntity<AuthTokenResponse> selectInstitution(
            @Valid @RequestBody SelectInstitutionRequest request,
            @CookieValue(
                    value = SELECTION_COOKIE,
                    required = false
            ) String selectionToken,
            HttpServletResponse response
    ) {
        AuthTokenResponse tokens =
                authService.selectInstitution(request, selectionToken);

        addAuthCookies(
                response,
                tokens.getToken(),
                tokens.getRefreshToken()
        );

        clearCookie(response, SELECTION_COOKIE);

        return ResponseEntity.ok(tokens);
    }

    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(
                    value = REFRESH_COOKIE,
                    required = false
            ) String refreshToken,
            HttpServletResponse response
    ) {
        try {
            authService.logout(refreshToken);
        } finally {
            clearCookie(response, ACCESS_COOKIE);
            clearCookie(response, REFRESH_COOKIE);
        }

        return ResponseEntity.noContent().build();
    }

    // EXIT INSTITUTION — SUPER ADMIN
    @PostMapping("/exit-institution")
    public ResponseEntity<Void> exitInstitution(
            HttpServletResponse response
    ) {
        AuthTokenResponse tokens = authService.exitInstitution();

        addAuthCookies(
                response,
                tokens.getToken(),
                tokens.getRefreshToken()
        );

        return ResponseEntity.noContent().build();
    }

    // CURRENT USER
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(
            Authentication authentication
    ) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        MeResponse currentUser =
                authQueryService.getCurrentUser(authentication.getName());

        return ResponseEntity.ok(currentUser);
    }

    // MY INSTITUTIONS
    @GetMapping("/my-institutions")
    public ResponseEntity<?> myInstitutions(
            Authentication authentication
    ) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(
                authQueryService.getMyInstitutions(authentication.getName())
        );
    }

    // REFRESH TOKEN
    @PostMapping("/refresh")
    public ResponseEntity<AuthTokenResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request,
            HttpServletResponse response
    ) {
        AuthTokenResponse tokens =
                authService.refreshToken(request.getRefreshToken());

        addAuthCookies(
                response,
                tokens.getToken(),
                tokens.getRefreshToken()
        );

        return ResponseEntity.ok(tokens);
    }

    // COOKIE HELPERS
    private void addAuthCookies(
            HttpServletResponse response,
            String accessToken,
            String refreshToken
    ) {
        if (accessToken != null && !accessToken.isBlank()) {
            addCookie(
                    response,
                    ACCESS_COOKIE,
                    accessToken,
                    ACCESS_COOKIE_MAX_AGE
            );
        }

        if (refreshToken != null && !refreshToken.isBlank()) {
            addCookie(
                    response,
                    REFRESH_COOKIE,
                    refreshToken,
                    REFRESH_COOKIE_MAX_AGE
            );
        }
    }

    private void addCookie(
            HttpServletResponse response,
            String name,
            String value,
            long maxAge
    ) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(secureCookies)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax")
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }

    private void clearCookie(
            HttpServletResponse response,
            String name
    ) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(secureCookies)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }
}

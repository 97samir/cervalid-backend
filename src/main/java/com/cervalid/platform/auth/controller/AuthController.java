package com.cervalid.platform.auth.controller;

import com.cervalid.platform.auth.dto.*;
import com.cervalid.platform.auth.service.AuthQueryService;
import com.cervalid.platform.auth.service.AuthService;
import com.cervalid.platform.security.context.UserContext;
import com.cervalid.platform.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthQueryService authQueryService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {
        LoginResponse loginResponse =
                authService.login(request.getEmail(), request.getPassword());
        // si el login ya generó el token
        if (loginResponse.getToken() != null)
            addAuthCookie(response, loginResponse.getToken());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/select-institution")
    public ResponseEntity<AuthTokenResponse> selectInstitution(
            @RequestBody SelectInstitutionRequest request,
            HttpServletResponse response) {

        log.info("=== SELECT INSTITUTION DEBUG ===");
        log.info("Request received: institutionUserId={}", request.getInstitutionUserId());
        log.info("Request received: institutionId={}", request.getInstitutionId());
        log.info("Request payload: " + request);
        // genera jwt y refreshToken
        try {

            AuthTokenResponse authTokenResponse = authService.selectInstitution(request);

            log.info("AuthTokenResponse generated: token={}", authTokenResponse.getToken());

            addAuthCookie(response, authTokenResponse.getToken());
            return ResponseEntity.ok(authTokenResponse);

        } catch (RuntimeException ex) {
            log.error("Error en select-institution: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthTokenResponse(null, ex.getMessage()));
        }
    }

    private static final Logger log =
            LoggerFactory.getLogger(AuthController.class);
    // leer perfil activo desde Spring
    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletResponse response,
            @CookieValue(value = "cervalid_token", required = false) String token) {

        boolean isProd = "prod".equalsIgnoreCase(activeProfile);

        log.info("=== AUTH LOGOUT DEBUG ===");
        log.info("Perfil activo: {}", activeProfile);
        log.info("¿Es producción?: {}", isProd);

        if (token == null) {
            log.warn("Logout llamado sin cookie activa");
            return ResponseEntity.ok("No había sesión activa");
        }

        log.info("Cookie encontrada, procediendo a limpiar sesión");

        ResponseCookie cookie = ResponseCookie.from("cervalid_token", "")
                .httpOnly(true)
                .secure(isProd)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok("Logout exitoso");
    }

    // salir de una institucion bajo contexto (caso superadmin)
    @PostMapping("/exit-institution")
    public ResponseEntity<?> exitInstitution(
            HttpServletResponse response) {

        AuthTokenResponse authTokenResponse = authService.exitInstitution();

        boolean isProd = "prod".equalsIgnoreCase(activeProfile);
        //setear nuevo token en cookie
        Cookie cookie = new Cookie("cervalid_token", authTokenResponse.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(isProd);
        cookie.setPath("/");
        cookie.setMaxAge(60*60); // 1 hora

        response.addCookie(cookie);

        Cookie refreshCookie = new Cookie("cervalid_refreshToken", authTokenResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(isProd);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshCookie);

        return ResponseEntity.ok().build();
    }

    // USUARIO ACTUAL (según JWT)
    @GetMapping("/me")
    public ResponseEntity<?> me(
            Authentication authentication,
            @CookieValue(value = "cervalid_token", required = false) String token) {

        log.info("Authentication principal: {}", authentication);
        log.info("Authentication authenticated: {}", authentication != null && authentication.isAuthenticated());

        if (token == null) {
            log.warn("No hay cookie, acceso rechazado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("ROLE DESDE JWT: {}", UserContext.getRole());
        log.info("INSTITUTION DESDE JWT: {}", UserContext.getInstitutionId());

        String email = authentication.getName();
        MeResponse response = authQueryService.getCurrentUser(email);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-institutions")
    public List<InstitutionOption> myInstitutions(Authentication authentication){

        String email = authentication.getName();
        return authQueryService.getMyInstitutions(email);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthTokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest request){

        AuthTokenResponse response =
                authService.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(response);
    }

    // metodo para enviar cookie a frontend
    private void addAuthCookie(HttpServletResponse response, String token) {

        boolean isProd = "prod".equalsIgnoreCase(activeProfile);

        ResponseCookie cookie = ResponseCookie.from("cervalid_token", token)
                .httpOnly(true)
                .secure(isProd) // true en producción con HTTPS
                .path("/")
                .maxAge(60 * 60 * 24) // 1 dia
                .sameSite("Lax") //"None"
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

}

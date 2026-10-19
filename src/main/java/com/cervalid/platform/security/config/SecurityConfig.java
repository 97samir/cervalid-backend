
package com.cervalid.platform.security.config;

import com.cervalid.platform.security.jwt.JwtAuthenticationFilter;
import com.cervalid.platform.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                // Respuestas de autenticación para la API REST
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                new HttpStatusEntryPoint(
                                        HttpStatus.UNAUTHORIZED
                                )
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Autenticación: acceso público controlado
                        .requestMatchers(
                                HttpMethod.POST,
                                "/auth/login",
                                "/auth/refresh",
                                "/auth/logout"
                        ).permitAll()

                        // Temporalmente requiere autenticación.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/auth/select-institution"
                        ).permitAll()

                        // Operaciones de sesión autenticada
                        .requestMatchers(
                                "/auth/me",
                                "/auth/my-institutions",
                                "/auth/exit-institution"
                        ).authenticated()

                        // Activación y solicitudes de institución
                        .requestMatchers("/activate").permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/institution-requests"
                        ).permitAll()

                        .requestMatchers("/institution-requests/**")
                        .hasRole("SUPER_ADMIN")

                        // Instituciones
                        .requestMatchers("/institutions")
                        .hasRole("SUPER_ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/institution/{id}"
                        ).hasRole("SUPER_ADMIN")

                        // Usuarios
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/users/{id}/role"
                        ).hasAnyRole(
                                "SUPER_ADMIN",
                                "INSTITUTION_ADMIN"
                        )

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/users/{id}"
                        ).hasAnyRole(
                                "SUPER_ADMIN",
                                "INSTITUTION_ADMIN"
                        )

                        .requestMatchers(HttpMethod.GET, "/users")
                        .hasAnyRole(
                                "SUPER_ADMIN",
                                "INSTITUTION_ADMIN",
                                "INSTITUTION_AUDITOR"
                        )

                        .requestMatchers(HttpMethod.GET, "/users/**")
                        .hasAnyRole(
                                "SUPER_ADMIN",
                                "INSTITUTION_ADMIN",
                                "INSTITUTION_AUDITOR",
                                "INSTITUTION_SUBADMIN"
                        )

                        .requestMatchers("/users/**")
                        .hasAnyRole(
                                "SUPER_ADMIN",
                                "INSTITUTION_ADMIN",
                                "INSTITUTION_SUBADMIN"
                        )

                        // Usuarios vinculados a instituciones
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/institution-users/**"
                        ).hasAnyRole(
                                "SUPER_ADMIN",
                                "INSTITUTION_ADMIN"
                        )

                        // Invitaciones
                        .requestMatchers("/invitations")
                        .hasAnyRole(
                                "SUPER_ADMIN",
                                "INSTITUTION_ADMIN",
                                "INSTITUTION_SUBADMIN"
                        )

                        // Verificación pública
                        .requestMatchers("/verification/**").permitAll()

                        // Emisión de certificados
                        .requestMatchers(
                                HttpMethod.POST,
                                "/certificates"
                        ).hasAnyRole(
                                "INSTITUTION_ADMIN",
                                "INSTITUTION_SUBADMIN"
                        )

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .userDetailsService(userDetailsService);

        return http.build();
    }
}

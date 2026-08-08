package com.cervalid.platform.security.config;

import com.cervalid.platform.security.jwt.JwtAuthenticationFilter;
import com.cervalid.platform.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        try {

            http
                    .cors(cors -> {})
                    .csrf(csrf -> csrf.disable())

                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )

                    .authorizeHttpRequests(auth -> auth

                            .requestMatchers("/institutions").hasRole("SUPER_ADMIN")

                            .requestMatchers("/institution-requests").permitAll()
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers("/activate").permitAll()

                            .requestMatchers("/institution-requests/**")
                            .hasRole("SUPER_ADMIN")

                            .requestMatchers(HttpMethod.PATCH, "/users/{id}/role")
                            .hasAnyRole("SUPER_ADMIN", "INSTITUTION_ADMIN")

                            .requestMatchers(HttpMethod.DELETE, "/users/{id}")
                            .hasAnyRole("SUPER_ADMIN", "INSTITUTION_ADMIN")

                            .requestMatchers(HttpMethod.GET, "/users")
                            .hasAnyRole("SUPER_ADMIN","INSTITUTION_ADMIN","INSTITUTION_AUDITOR")

                            .requestMatchers(HttpMethod.GET, "/institutions")
                            .hasRole("SUPER_ADMIN")

                            .requestMatchers(HttpMethod.GET, "/users/**")
                            .hasAnyRole("SUPER_ADMIN", "INSTITUTION_ADMIN", "INSTITUTION_AUDITOR",
                                    "INSTITUTION_SUBADMIN")

                            .requestMatchers(HttpMethod.PATCH, "/institution/{id}")
                            .hasRole("SUPER_ADMIN")

                            .requestMatchers(HttpMethod.PATCH, "/institution-users/**")
                            .hasAnyRole("SUPER_ADMIN", "INSTITUTION_ADMIN")

                            .requestMatchers("/users/**")
                            .hasAnyRole("SUPER_ADMIN", "INSTITUTION_ADMIN",
                                    "INSTITUTION_SUBADMIN")

                            .requestMatchers( "/invitations")
                            .hasAnyRole("SUPER_ADMIN", "INSTITUTION_ADMIN",
                                    "INSTITUTION_SUBADMIN")

                            .requestMatchers("/verification/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/certificates")
                            .hasAnyRole("INSTITUTION_ADMIN", "INSTITUTION_SUBADMIN")

                            // TODO LO DEMÁS AUTENTICADO
                            .anyRequest().authenticated()
                    )

                    .addFilterBefore(
                            jwtAuthenticationFilter,
                            UsernamePasswordAuthenticationFilter.class
                    )

                    .userDetailsService(userDetailsService);

            return http.build();

        } catch (Exception e) {

            System.out.println("ERROR EN SECURITY CONFIG");
            e.printStackTrace();

            throw e;
        }
    }
}
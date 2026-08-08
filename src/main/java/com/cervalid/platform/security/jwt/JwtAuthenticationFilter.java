package com.cervalid.platform.security.jwt;
// Lee el token, Valida, Carga usuario, Inyecta institutionId al contexto
import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.security.context.UserContext;
import com.cervalid.platform.security.service.CustomUserDetailsService;
import com.cervalid.platform.security.user.CustomUserDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String token = null;
            // intenta obtener desde authorization header
            final String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
            // si no hay header -> buscar en cookies
            if (token == null && request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("cervalid_token".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }
            // si no hay token -> continuar
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            boolean isValid = false;

            try {
                isValid = jwtService.isTokenValid(token);
            } catch (Exception e) {
                System.out.println("ERROR VALIDANDO TOKEN: " + e.getMessage());
            }

            System.out.println("TOKEN VALIDO: " + isValid);

            if (!isValid) {
                System.out.println("TOKEN INVALIDO - NO AUTENTICA");
                filterChain.doFilter(request, response);
                return;
            }

            // Extraer claims
            Claims claims = jwtService.extractAllClaims(token);
            String username = claims.getSubject();

            // EXTRAER ROLE E INSTITUTION
            String roleClaim = claims.get("role", String.class);
            Object institutionIdClaim = claims.get("institutionId");

            // Cargar usuario Spring Security
            CustomUserDetails userDetails;
            try {
                userDetails = (CustomUserDetails)
                        userDetailsService.loadUserByUsername(username);
            } catch (Exception e) {
                System.out.println("USUARIO JWT NO EXISTE");
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            System.out.println("=== JWT DEBUG ===");
            System.out.println("ROLE JWT: " + roleClaim);
            System.out.println("AUTHORITIES SET: " + userDetails.getAuthorities());
            System.out.println("INSTITUTION JWT: " + institutionIdClaim);

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            // Inyectar datos al UserContext
            UserContext.setUserId(userDetails.getUserId());
            UserContext.setInstitutionUserId(
                    userDetails.getInstitutionUserId()
            );

            if (roleClaim != null) {
                UserContext.setRole(RoleName.valueOf(roleClaim));
            }

            if (institutionIdClaim != null) {
                try {
                    Long institutionId = Long.valueOf(institutionIdClaim.toString());

                    if (institutionId <= 0) {
                        throw new IllegalArgumentException("Invalid institutionId");
                    }

                    UserContext.setInstitutionId(institutionId);

                } catch (Exception e) {
                    System.out.println("ERROR parsing institutionId: " + institutionIdClaim);
                    e.printStackTrace();

                    // NO ROMPE REQUEST
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            // DEBUG
            System.out.println("ROL JWT: " + roleClaim);
            System.out.println("INSTITUTION JWT: " + institutionIdClaim);

            filterChain.doFilter(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            // NO rompe servidor
            filterChain.doFilter(request, response);

        } finally {
            //Limpieza obligatoria del contexto por seguridad
            SecurityContextHolder.clearContext();
            UserContext.clear();

        }
    }
}

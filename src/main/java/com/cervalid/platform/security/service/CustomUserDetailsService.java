package com.cervalid.platform.security.service;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.security.permissions.Role;
import com.cervalid.platform.security.permissions.RolePermission;
import com.cervalid.platform.security.permissions.repository.RolePermissionRepository;
import com.cervalid.platform.security.permissions.repository.RoleRepository;
import com.cervalid.platform.security.user.CustomUserDetails;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final InstitutionUserRepository institutionUserRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        try {
            //System.out.println("LOGIN ATTEMPT: " + email);
            User user = userRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Usuario no encontrado"));

            if (!user.isActive()) {
                throw new DisabledException("Usuario inactivo");
            }

            // ===== SUPER ADMIN =====
            if (user.getGlobalRole() == RoleName.SUPER_ADMIN) {

                //System.out.println("ROL DETECTADO: SUPER_ADMIN");

                List<GrantedAuthority> authorities = new ArrayList<>();

                authorities.add(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getGlobalRole().name()
                        )
                );

                Role role = roleRepository.findByName(RoleName.SUPER_ADMIN)
                        .orElseThrow(() ->
                                new RuntimeException("Rol SUPER_ADMIN no encontrado"));

                List<RolePermission> rolePermissions =
                        rolePermissionRepository.findByRole(role);

                for (RolePermission rp : rolePermissions) {

                    authorities.add(
                            new SimpleGrantedAuthority(
                                    rp.getPermission().getCode()
                            )
                    );
                }

                //System.out.println("AUTHORITIES CARGADAS:");
                for (GrantedAuthority a : authorities) {
                    //System.out.println(a.getAuthority());
                }

                return new CustomUserDetails(
                        user.getId(),
                        null,
                        user.getEmail(),
                        user.getPassword(),
                        authorities
                );
            }

            // ===== USUARIO NORMAL =====
            InstitutionUser relation =
                    institutionUserRepository
                            .findFirstByUserIdAndActiveTrue(user.getId())
                            .orElseThrow(() ->
                                    new RuntimeException("Usuario sin institución"));

            if (!relation.isActive()) {
                throw new DisabledException(
                        "Usuario inactivo en esta institución"
                );
            }

            Long institutionId = relation.getInstitution().getId();

            List<GrantedAuthority> authorities = new ArrayList<>();

            List<RolePermission> rolePermissions =
                    rolePermissionRepository.findByRole(relation.getRole());

            for (RolePermission rp : rolePermissions) {

                authorities.add(
                        new SimpleGrantedAuthority(
                                rp.getPermission().getCode()
                        )
                );
            }

            authorities.add(
                    new SimpleGrantedAuthority(
                            "ROLE_" + relation.getRole().getName().name()
                    )
            );

            //System.out.println("AUTHORITIES CARGADAS:");
            for (GrantedAuthority a : authorities) {
                //System.out.println(a.getAuthority());
            }

            return new CustomUserDetails(
                    user.getId(),
                    institutionId,
                    user.getEmail(),
                    user.getPassword(),
                    authorities
            );

        } catch (Exception e) {

            System.out.println("ERROR EN CustomUserDetailsService");
            e.printStackTrace();

            throw e;
        }
    }
}
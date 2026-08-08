package com.cervalid.platform.user.repository;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionUserRepository
        extends JpaRepository<InstitutionUser, Long> {

    Optional<InstitutionUser> findByUserIdAndInstitutionId(
            Long userId,
            Long institutionId
    );

    // Método con JOIN FETCH para traer institución y role
    @Query("SELECT iu FROM InstitutionUser iu " +
            "JOIN FETCH iu.institution " +
            "JOIN FETCH iu.role " +
            "WHERE iu.id = :id")
    Optional<InstitutionUser> findByIdWithInstitutionAndRole(
            @Param("id") Long id);

    Optional<InstitutionUser> findByUserId(
            Long userId
    );

    List<InstitutionUser> findAllByUserId(
            Long userId
    );

    Optional<InstitutionUser> findByUserEmailAndInstitutionId(
            String email,
            Long institutionId
    );

    Optional<InstitutionUser> findFirstByUserId(
            Long userId
    );

    Optional<InstitutionUser> findFirstByUserIdAndActiveTrue(
            Long userId
    );

    //para el login
    List<InstitutionUser> findByUserIdAndActiveTrue(
            Long userId
    );

    // consultas dinamicas para listar usuarios
    Page<InstitutionUser> findAllByInstitution_Id(
            Long institutionId,
            Pageable pageable);

    Page<InstitutionUser> findAllByRole_Name(
            RoleName roleName,
            Pageable pageable);

    Page<InstitutionUser> findAllByInstitution_IdAndRole_Name(
            Long institutionId,
            RoleName roleName,
            Pageable pageable
    );

    Page<InstitutionUser> findAllByInstitution_IdAndRole_NameAndActive(
            Long institutionId,
            RoleName roleName,
            boolean active,
            Pageable pageable
    );

    Page<InstitutionUser> findAllByInstitution_IdAndActive(
            Long institutionId,
            boolean active,
            Pageable pageable
    );

    // listar usuarios para el frontend superadmin - version global sin institucion
    Page<InstitutionUser> findAllByRole_NameAndActive(
            RoleName role,
            boolean active,
            Pageable pageable
    );

    // listar usuarios para el frontend superadmin - version global sin isntitucion
    Page<InstitutionUser> findAllByActive(
            boolean active,
            Pageable pageable
    );

    // para dashboard de frontend
    long countByInstitutionIdAndActiveTrue (
            Long institutionId
    );

    long countByInstitutionIdAndRole_NameAndActiveTrue (
            Long institutionId,
            RoleName roleName
    );

    @Query("""
        SELECT COUNT(iu)
        FROM InstitutionUser iu
        WHERE iu.institution.id = :institutionId
        AND iu.active = true
        AND iu.role.name IN :roles
    """)
    long countByInstitutionAndRoles(
            @Param("institutionId") Long institutionId,
            @Param("roles") List<RoleName> roles
    );
}

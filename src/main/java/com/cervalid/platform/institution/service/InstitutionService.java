package com.cervalid.platform.institution.service;

import com.cervalid.platform.audit.annotation.Auditable;
import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.institution.dto.*;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.security.context.UserContext;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.tenant.enums.RequestStatus;
import com.cervalid.platform.tenant.repository.InstitutionRequestRepository;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository institutionUserRepository;

    @Auditable(action = "CREATE", resource = "INSTITUTION")

    public InstitutionResponse createInstitution(CreateInstitutionRequest request) {

        RoleName role = UserContext.getRole();

        if (role != RoleName.SUPER_ADMIN) {
            throw new RuntimeException("Solo SUPER_ADMIN puede crear instituciones");
        }

        Institution institution = new Institution();
        institution.setName(request.getName());
        institution.setRuc(request.getRuc());
        institution.setActive(true);

        System.out.println("REQUEST NAME: " + request.getName());
        System.out.println("REQUEST RUC: " + request.getRuc());
        System.out.println("ROLE: " + UserContext.getRole());

        Institution saved = institutionRepository.save(institution);

        return mapToResponse(saved);
    }

    public void updateInstitution(Long id, UpdateInstitutionRequest request) {

        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institución no encontrada"));

        if (request.getName() != null) {
            institution.setName(request.getName());
        }

        if (request.getRuc() != null) {
            institution.setRuc(request.getRuc());
        }

        if (request.getActive() != null) {
            institution.setActive(request.getActive());
        }

        institutionRepository.save(institution);
    }

    // datos propios de la institución
    public InstitutionResponse getMyInstitution() {

        Long institutionId = UserContext.getInstitutionId();

        if (institutionId == null) {
            throw new RuntimeException("Usuario sin institución");
        }

        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Institución no encontrada"));

        return mapToResponse(institution);
    }

    // dashboard para el frontend conteo de usuarios
    public DashboardStatsResponse getDashboardStats(){

        Long institutionId = UserContext.getInstitutionId();

        if(institutionId == null){
            throw new RuntimeException("No hay institución en el contexto");
        }

        long totalUsers =
                institutionUserRepository.countByInstitutionIdAndActiveTrue(institutionId);

        long totalStudents =
                institutionUserRepository.countByInstitutionIdAndRole_NameAndActiveTrue(
                        institutionId, RoleName.STUDENT
                );

        long totalAdmins =
                institutionUserRepository.countByInstitutionAndRoles(
                        institutionId,
                        List.of(
                                RoleName.INSTITUTION_ADMIN,
                                RoleName.INSTITUTION_SUBADMIN
                        )
                );

        return DashboardStatsResponse.builder()
                .totalUsers(totalUsers)
                .totalStudents(totalStudents)
                .totalAdmins(totalAdmins)
                .totalCertificates(0L) // futuro
                .totalVerified(0L)     // futuro
                .build();
    }

    // metodo para listar todas las instituciones activas
    public List<InstitutionResponse> listInstitutions(Boolean active){

        try {

            List<Institution> institutions;

            if (active != null) {
                institutions = institutionRepository.findByActive(active);
            } else {
                institutions = institutionRepository.findAll();
            }

            return institutions.stream()
                    .map(this::mapToResponse)
                    .toList();

        } catch (Exception e){

            System.err.println("ERROR EN listInstitutions()");
            System.err.println(e.getMessage());

            throw new RuntimeException("Error listando instituciones: " + e.getMessage(), e);
        }
    }

    // actualizar estado activo o inactivo
    public void changeStatus(Long id, boolean active) {

        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institución no encontrada"));

        institution.setActive(active);

        institutionRepository.save(institution);
    }

    private InstitutionResponse mapToResponse(Institution institution) {

        return InstitutionResponse.builder()
                .id(institution.getId())
                .name(institution.getName())
                .ruc(institution.getRuc())
                .type(institution.getType())
                .country(institution.getCountry())
                .city(institution.getCity())
                .address(institution.getAddress())

                .tienePresenciaDigital(institution.isTienePresenciaDigital())
                .website(institution.getWebsite())
                .description(institution.getDescription())
                .institutionalEmail(institution.getInstitutionalEmail())
                .institutionalDominio(institution.getInstitutionalDominio())

                .active(institution.getActive())
                .build();
    }

}


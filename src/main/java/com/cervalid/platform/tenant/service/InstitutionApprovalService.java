package com.cervalid.platform.tenant.service;

import com.cervalid.platform.blockchain.service.BlockchainAdminService;
import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.security.permissions.Role;
import com.cervalid.platform.security.permissions.repository.RoleRepository;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.tenant.dto.ApproveInstitutionResponse;
import com.cervalid.platform.tenant.entity.InstitutionRequest;
import com.cervalid.platform.tenant.enums.RequestStatus;
import com.cervalid.platform.tenant.repository.InstitutionRequestRepository;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InstitutionApprovalService {

    private final InstitutionRequestRepository requestRepository;
    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final InstitutionUserRepository institutionUserRepository;
    private final BlockchainAdminService blockchainAdminService;

    @Transactional
    public ApproveInstitutionResponse approveRequest(
            Long requestId,
            User superAdmin,
            String walletAddress) {

        InstitutionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("La solicitud ya fue procesada");
        }

        // validar wallet
        if (walletAddress == null || walletAddress.isBlank()) {
            throw new RuntimeException(
                    "Wallet address es requerida");
        }
        // Crear tenant (Institution real)
        Institution institution = mapToInstitution(request);
        // guardar wallet en institución
        institution.setWalletAddress(walletAddress);
        Institution savedInstitution =
                institutionRepository.save(institution);
        // registrar institucion en blockchain
        String txHash =
                blockchainAdminService.registerInstitution(walletAddress);

        // Crear Admin institucional
        User admin = new User();
        admin.setName(request.getName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.resolveLoginEmail());
        admin.setPassword(request.getPassword()); // ya viene encriptado
        admin.setDocumentType(request.getDocumentType());
        admin.setDocument(request.getDocument());
        admin.setPhone(request.getPhone());
        admin.setActive(true);

        User savedUser = userRepository.save(admin);

        // obtener rol
        Role adminRole = roleRepository
                .findByName(RoleName.INSTITUTION_ADMIN)
                .orElseThrow();
        // crear relación usuario < - > institución
        InstitutionUser relation = new InstitutionUser();

        relation.setUser(savedUser);
        relation.setInstitution(savedInstitution);
        relation.setRole(adminRole);
        relation.setActive(true);

        institutionUserRepository.save(relation);

        // Actualizar estado solicitud
        request.setStatus(RequestStatus.APPROVED);
        request.setApprovedBy(superAdmin);
        request.setApprovedAt(LocalDateTime.now());

        requestRepository.save(request);

        return ApproveInstitutionResponse.builder()
                .institutionId(savedInstitution.getId())
                .institutionName(savedInstitution.getName())
                .walletAddress(savedInstitution.getWalletAddress())
                .approved(true)
                .blockchainTxHash(txHash)
                .status(RequestStatus.APPROVED)
                .message("Institution approved successfully")
                .build();
    }

    // metodo rechazo institución
    public void rejectRequest(Long requestId, String reason, User superAdmin) {

        InstitutionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("La solicitud ya fue procesada");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectedBy(superAdmin);
        request.setRejectedAt(LocalDateTime.now());
        request.setRejectReason(reason);

        requestRepository.save(request);
    }

    private Institution mapToInstitution(InstitutionRequest request) {

        Institution inst = new Institution();

        inst.setName(request.getInstitutionName());
        inst.setRuc(request.getRuc());
        inst.setType(request.getInstitutionType());
        inst.setCountry(request.getCountry());
        inst.setCity(request.getCity());
        inst.setAddress(request.getAddress());

        inst.setTienePresenciaDigital(
                request.getTienePresenciaDigital() != null
                        ? request.getTienePresenciaDigital()
                        : false
        );

        // manejar opcionales correctamente
        inst.setWebsite(
                Boolean.TRUE.equals(request.getTienePresenciaDigital())
                        ? request.getWebsite()
                        : "N/A"
        );

        inst.setDescription(
                Boolean.TRUE.equals(request.getTienePresenciaDigital())
                        ? request.getDescription()
                        : "Sin descripción"
        );

        inst.setInstitutionalEmail(
                request.getInstitutionalEmail() != null
                        ? request.getInstitutionalEmail()
                        : request.getContactEmail()
        );

        inst.setInstitutionalDominio(
                request.getInstitutionalDominio() != null
                        ? request.getInstitutionalDominio()
                        : "N/A"
        );

        inst.setActive(true);

        return inst;
    }

}

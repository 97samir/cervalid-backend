// Calidar campos y Guardar solicitud en estado PENDING
package com.cervalid.platform.tenant.service;

import com.cervalid.platform.common.util.DomainUtils;
import com.cervalid.platform.tenant.dto.InstitutionRequestDTO;
import com.cervalid.platform.tenant.dto.InstitutionRequestResponse;
import com.cervalid.platform.tenant.entity.InstitutionRequest;
import com.cervalid.platform.tenant.enums.RequestStatus;
import com.cervalid.platform.tenant.repository.InstitutionRequestRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionRequestService {

    private final InstitutionRequestRepository requestRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public InstitutionRequestResponse registerRequest(InstitutionRequestDTO dto) {

        // ------------- normalización a minúsculas -------------
        if (dto.getInstitutionalEmail() != null) {
            dto.setInstitutionalEmail(
                    dto.getInstitutionalEmail().toLowerCase().trim()
            );
        }

        if (dto.getInstitutionalDominio() != null) {
            dto.setInstitutionalDominio(
                    dto.getInstitutionalDominio().toLowerCase().trim()
            );
        }

        if (dto.getContactEmail() != null) {
            dto.setContactEmail(
                    dto.getContactEmail().toLowerCase().trim()
            );
        }

        // Normalizar campos opcionales
        normalizeOptionalFields(dto);

        // Validar coherencia dominio - email institucional
        if (dto.getInstitutionalEmail() != null &&
                dto.getInstitutionalDominio() != null) {

            String emailDomain = DomainUtils.extractDomainFromEmail(dto.getInstitutionalEmail());

            if (!emailDomain.equalsIgnoreCase(dto.getInstitutionalDominio())) {
                throw new RuntimeException(
                        "El dominio del correo institucional no coincide con el dominio institucional"
                );
            }
        }

        // Validar coherencia dominio - website
        if (dto.getWebsite() != null &&
                dto.getInstitutionalDominio() != null) {

            String websiteDomain = DomainUtils.extractDomainFromUrl(dto.getWebsite());


            if (!websiteDomain.endsWith(dto.getInstitutionalDominio())) {
                throw new RuntimeException(
                        "El dominio del sitio web no coincide con el dominio institucional"
                );
            }
        }

        // Validar nombre de institucion duplicado
        requestRepository.findByInstitutionName(dto.getInstitutionName())
                .ifPresent(r -> {
                    throw new RuntimeException("Ya existe una solicitud con este Nombre de institución");
                });

        // Validar RUC duplicado
        requestRepository.findByRuc(dto.getRuc())
                .ifPresent(r -> {
                    throw new RuntimeException("Ya existe una solicitud con este RUC");
                });

        // Validar documento dni / ce  duplicado
        userRepository.findByDocument(dto.getDocument())
                .ifPresent(r -> {
                    throw new RuntimeException("Ya existe una solicitud con este DNI / CE");
                });

        // Validar email institucional duplicado
        if (dto.getInstitutionalEmail() != null) {
            requestRepository.findByInstitutionalEmail(dto.getInstitutionalEmail())
                    .ifPresent(r -> {
                        throw new RuntimeException("Ya existe una solicitud con este correo institucional");
                    });
        }

        // mapeo dto -> entity
        InstitutionRequest entity = mapToEntity(dto);
        // Encriptar contraseña
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        // Estado inicial
        entity.setStatus(RequestStatus.PENDING);

        InstitutionRequest saved = requestRepository.save(entity);

        return mapToResponse(saved);
    }

 /*
    // Limpia y normaliza campos opcionales según presencia digital
    private void normalizeOptionalFields(InstitutionRequest request) {
        // Convertir "" a null
        request.setInstitutionalEmail(blankToNull(request.getInstitutionalEmail()));
        request.setWebsite(blankToNull(request.getWebsite()));
        request.setInstitutionalDominio(blankToNull(request.getInstitutionalDominio()));
        request.setDescription(blankToNull(request.getDescription()));

        // Si NO tiene presencia digital → limpiar todo
        if (Boolean.FALSE.equals(request.getTienePresenciaDigital())) {
            request.setInstitutionalEmail(null);
            request.setWebsite(null);
            request.setInstitutionalDominio(null);
            request.setDescription(null);
        }
    }
*/
    // versión para dto
    private void normalizeOptionalFields(InstitutionRequestDTO dto) {

        dto.setInstitutionalEmail(blankToNull(dto.getInstitutionalEmail()));
        dto.setWebsite(blankToNull(dto.getWebsite()));
        dto.setInstitutionalDominio(blankToNull(dto.getInstitutionalDominio()));
        dto.setDescription(blankToNull(dto.getDescription()));

        if (Boolean.FALSE.equals(dto.getTienePresenciaDigital())) {
            dto.setInstitutionalEmail(null);
            dto.setWebsite(null);
            dto.setInstitutionalDominio(null);
            dto.setDescription(null);
        }
    }

    private String blankToNull(String value) {
        return (
                value == null || value.trim().isEmpty()
        ) ? null :
                value.trim();
    }

    // listar instituciones
    public List<InstitutionRequestResponse> getAllRequests() {

        List<InstitutionRequest> requests = requestRepository.findAll();

        return requests.stream().map(this::mapToResponse).toList();
    }

    // obtener estado
    public List<InstitutionRequestResponse> getRequestsByStatus(String status) {

        RequestStatus enumStatus;

        try {
            enumStatus = RequestStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Estado inválido");
        }

        List<InstitutionRequest> requests =
                requestRepository.findByStatus(enumStatus);

        return requests.stream().map(this::mapToResponse).toList();
    }

    // metodo evita dupplicación de datos en otros metodos
    private InstitutionRequestResponse mapToResponse(InstitutionRequest req) {

        InstitutionRequestResponse r = new InstitutionRequestResponse();

        r.setId(req.getId());
        r.setInstitutionName(req.getInstitutionName());
        r.setRuc(req.getRuc());
        r.setInstitutionType(req.getInstitutionType());
        r.setCountry(req.getCountry());
        r.setCity(req.getCity());
        r.setAddress(req.getAddress());
        r.setName(req.getName());
        r.setLastName(req.getLastName());
        r.setDocumentType(req.getDocumentType());
        r.setDocument(req.getDocument());
        r.setPhone(req.getPhone());
        r.setPosition(req.getPosition());
        r.setContactEmail(req.getContactEmail());
        r.setDocumentAcreditationUrl(req.getDocumentAcreditationUrl());
        r.setWebsite(req.getWebsite());
        r.setDescription(req.getDescription());
        r.setInstitutionalEmail(req.getInstitutionalEmail());
        r.setInstitutionalDominio(req.getInstitutionalDominio());
        r.setStatus(req.getStatus().name());

        return r;
    }

    // mapeo dto a -> entity
    private InstitutionRequest mapToEntity(InstitutionRequestDTO dto) {

        InstitutionRequest req = new InstitutionRequest();

        req.setInstitutionName(dto.getInstitutionName());
        req.setRuc(dto.getRuc());
        req.setInstitutionType(dto.getInstitutionType());
        req.setCountry(dto.getCountry());
        req.setCity(dto.getCity());
        req.setAddress(dto.getAddress());

        req.setName(dto.getName());
        req.setLastName(dto.getLastName());

        req.setDocumentType(dto.getDocumentType());
        req.setDocument(dto.getDocument());
        req.setPhone(dto.getPhone());

        req.setPosition(dto.getPosition());
        req.setContactEmail(dto.getContactEmail());
        req.setPassword(dto.getPassword());

        req.setDocumentAcreditationUrl(dto.getDocumentAcreditationUrl());

        req.setTienePresenciaDigital(dto.getTienePresenciaDigital());
        req.setWebsite(dto.getWebsite());
        req.setDescription(dto.getDescription());
        req.setInstitutionalEmail(dto.getInstitutionalEmail());
        req.setInstitutionalDominio(dto.getInstitutionalDominio());

        return req;
    }

}

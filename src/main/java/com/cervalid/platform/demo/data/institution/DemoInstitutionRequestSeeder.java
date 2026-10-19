package com.cervalid.platform.demo.data.institution;

import com.cervalid.platform.common.validation.DocumentType;
import com.cervalid.platform.tenant.entity.InstitutionRequest;
import com.cervalid.platform.tenant.enums.RequestStatus;
import com.cervalid.platform.tenant.repository.InstitutionRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DemoInstitutionRequestSeeder {

    private final InstitutionRequestRepository requestRepository;
    private final PasswordEncoder passwordEncoder;

    private record DemoRequest(
            String institutionName,
            String ruc,
            String institutionType,
            String city,
            String name,
            String lastName,
            String document,
            String phone,
            String position,
            String contactEmail
    ) {}

    private static final List<DemoRequest> DEMO_REQUESTS = List.of(
            new DemoRequest(
                    "Universidad Horizonte Demo",
                    "20900000104",
                    "Universidad",
                    "Lima",
                    "Carlos",
                    "Ramírez",
                    "81000001",
                    "910000001",
                    "Director académico",
                    "carlos.ramirez@demo.cervalid.com"
            ),
            new DemoRequest(
                    "Instituto Tecnológico Andino Demo",
                    "20900000201",
                    "Instituto",
                    "Arequipa",
                    "María",
                    "Torres",
                    "81000002",
                    "910000002",
                    "Directora general",
                    "maria.torres@demo.cervalid.com"
            ),
            new DemoRequest(
                    "Colegio Nuevo Horizonte Demo",
                    "20900000309",
                    "Colegio",
                    "Cusco",
                    "Luis",
                    "Mendoza",
                    "81000003",
                    "910000003",
                    "Promotor educativo",
                    "luis.mendoza@demo.cervalid.com"
            ),
            new DemoRequest(
                    "Universidad del Pacífico Demo",
                    "20900000406",
                    "Universidad",
                    "Trujillo",
                    "Ana",
                    "Flores",
                    "81000004",
                    "910000004",
                    "Representante legal",
                    "ana.flores@demo.cervalid.com"
            ),
            new DemoRequest(
                    "Instituto Superior del Sur Demo",
                    "20900000503",
                    "Instituto",
                    "Tacna",
                    "Jorge",
                    "Castro",
                    "81000005",
                    "910000005",
                    "Director institucional",
                    "jorge.castro@demo.cervalid.com"
            ),
            new DemoRequest(
                    "Colegio Excelencia Académica Demo",
                    "20900000601",
                    "Colegio",
                    "Piura",
                    "Lucía",
                    "Vargas",
                    "81000006",
                    "910000006",
                    "Directora",
                    "lucia.vargas@demo.cervalid.com"
            )
    );

    @Transactional
    public void seed() {
        int created = 0;

        for (DemoRequest demo : DEMO_REQUESTS) {
            if (requestRepository.findByRuc(demo.ruc()).isPresent()) {
                continue;
            }

            InstitutionRequest request = new InstitutionRequest();

            request.setInstitutionName(demo.institutionName());
            request.setRuc(demo.ruc());
            request.setInstitutionType(demo.institutionType());
            request.setCountry("Perú");
            request.setCity(demo.city());
            request.setAddress("Dirección de demostración, " + demo.city());

            request.setName(demo.name());
            request.setLastName(demo.lastName());
            request.setDocumentType(DocumentType.DNI);
            request.setDocument(demo.document());
            request.setPhone(demo.phone());
            request.setPosition(demo.position());
            request.setContactEmail(demo.contactEmail());

            request.setPassword(
                    passwordEncoder.encode("Demo@2026-Cervalid")
            );

            request.setDocumentAcreditationUrl(
                    "https://example.com/demo/acreditacion/"
                            + demo.ruc()
            );

            request.setTienePresenciaDigital(false);
            request.setWebsite(null);
            request.setInstitutionalDominio(null);
            request.setInstitutionalEmail(null);
            request.setDescription(
                    "Solicitud de demostración para validar "
                            + "el proceso de incorporación institucional."
            );

            request.setStatus(RequestStatus.PENDING);

            requestRepository.save(request);
            created++;
        }

        log.info(
                "DemoInstitutionRequestSeeder: {} solicitudes nuevas creadas",
                created
        );
    }
}


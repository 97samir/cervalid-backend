package com.cervalid.platform.demo.data.institution;

import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DemoInstitutionSeeder {

    public static final String UNIVERSITY_RUC = "DEMO-CERV-001";
    public static final String INSTITUTE_RUC = "DEMO-CERV-002";

    private final InstitutionRepository institutionRepository;

    @Transactional
    public void seed() {
        createIfMissing(
                UNIVERSITY_RUC,
                "Universidad Demo Cervalid",
                "UNIVERSIDAD",
                "Av. Universidad 100, Surco, Lima",
                "universidad-demo@cervalid.local",
                "https://example.com"
        );

        createIfMissing(
                INSTITUTE_RUC,
                "Instituto Demo Cervalid",
                "INSTITUTO",
                "Av. Educación 200, Surco, Lima",
                "instituto-demo@cervalid.local",
                "https://example.org"
        );
    }

    private void createIfMissing(
            String ruc,
            String name,
            String type,
            String address,
            String email,
            String website
    ) {
        if (institutionRepository.existsByRuc(ruc)) {
            return;
        }

        Institution institution = new Institution();
        institution.setName(name);
        institution.setRuc(ruc);
        institution.setType(type);
        institution.setCountry("Perú");
        institution.setCity("Lima");
        institution.setAddress(address);
        institution.setTienePresenciaDigital(true);
        institution.setWebsite(website);
        institution.setDescription(
                "Institución ficticia para pruebas de Cervalid."
        );
        institution.setInstitutionalEmail(email);
        institution.setInstitutionalDominio("cervalid.local");
        institution.setActive(true);

        institutionRepository.save(institution);
    }
}
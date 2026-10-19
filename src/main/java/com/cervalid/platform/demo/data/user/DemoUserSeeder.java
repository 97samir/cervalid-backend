package com.cervalid.platform.demo.data.user;

import com.cervalid.platform.common.validation.DocumentType;
import com.cervalid.platform.demo.config.DemoDataProperties;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DemoUserSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DemoDataProperties properties;

    @Transactional
    public void seed() {

        String rawPassword = properties.getPassword();

        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalStateException(
                    "Configura CERVALID_DEMO_PASSWORD para cargar "
                            + "los datos demo."
            );
        }

        List<DemoUser> users = List.of(
                new DemoUser("demo.admin@cervalid.local",
                        "Ana", "Universidad", "700000001",
                        "900000001"),

                new DemoUser("demo.subadmin@cervalid.local",
                        "Luis", "Universidad", "700000002",
                        "900000002"),

                new DemoUser("demo.student01@cervalid.local",
                        "María", "Universidad", "700000003",
                        "900000003"),

                new DemoUser("demo.student02@cervalid.local",
                        "Carlos", "Universidad", "700000004",
                        "900000004"),

                new DemoUser("demo.recruiter@cervalid.local",
                        "Elena", "Universidad", "700000005",
                        "900000005"),

                new DemoUser("demo.instituto.admin@cervalid.local",
                        "Pedro", "Instituto", "700000006",
                        "900000006"),

                new DemoUser("demo.instituto.subadmin@cervalid.local",
                        "Lucía", "Instituto", "700000007",
                        "900000007"),

                new DemoUser("demo.instituto.student01@cervalid.local",
                        "Jorge", "Instituto", "700000008",
                        "900000008"),

                new DemoUser("demo.instituto.student02@cervalid.local",
                        "Rosa", "Instituto", "700000009",
                        "900000009"),

                new DemoUser("demo.instituto.auditor@cervalid.local",
                        "Mario", "Instituto", "700000010",
                        "900000010"),

                new DemoUser("demo.multiple@cervalid.local",
                        "Alex", "Compartido", "700000011",
                        "900000011")
        );

        for (DemoUser demo : users) {
            if (userRepository.findByEmailIgnoreCase(demo.email())
                    .isPresent()) {
                continue;
            }

            User user = new User();
            user.setName(demo.name());
            user.setLastName(demo.lastName());
            user.setEmail(demo.email());
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setDocumentType(DocumentType.DNI);
            user.setDocument(demo.document());
            user.setPhone(demo.phone());
            user.setActive(true);

            userRepository.save(user);
        }
    }

    private record DemoUser(
            String email,
            String name,
            String lastName,
            String document,
            String phone
    ) {}
}
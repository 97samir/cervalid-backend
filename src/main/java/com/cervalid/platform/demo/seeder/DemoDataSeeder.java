package com.cervalid.platform.demo.seeder;

import com.cervalid.platform.demo.config.DemoDataProperties;
import com.cervalid.platform.demo.data.academic.*;
import com.cervalid.platform.demo.data.institution.DemoInstitutionRequestSeeder;
import com.cervalid.platform.demo.data.institution.DemoInstitutionSeeder;
import com.cervalid.platform.demo.data.invitation.DemoInvitationSeeder;
import com.cervalid.platform.demo.data.user.DemoMembershipSeeder;
import com.cervalid.platform.demo.data.user.DemoUserSeeder;
import com.cervalid.platform.demo.data.verification.DemoVerificationSeeder;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DemoDataSeeder implements ApplicationRunner {

    private final DemoDataProperties properties;
    private final DemoInstitutionSeeder institutionSeeder;
    private final DemoUserSeeder userSeeder;
    private final DemoMembershipSeeder membershipSeeder;

    private final DemoStudentSeeder studentSeeder;
    private final DemoProfileSeeder profileSeeder;

    private final DemoTranscriptSeeder transcriptSeeder;
    private final DemoTranscriptItemSeeder transcriptItemSeeder;


    private final DemoAchievementSeeder achievementSeeder;
    private final DemoCompetencySeeder competencySeeder;

    private final DemoCertificateSeeder certificateSeeder;
    private final DemoCredentialSeeder credentialSeeder;

    private final DemoVerificationSeeder verificationSeeder;

    private final DemoInstitutionRequestSeeder institutionRequestSeeder;
    private final DemoInvitationSeeder invitationSeeder;

    @Override
    public void run(ApplicationArguments args) {
        if (!properties.isEnabled()) {
            return;
        }

        institutionSeeder.seed();
        userSeeder.seed();
        membershipSeeder.seed();

        studentSeeder.seed();
        profileSeeder.seed();

        transcriptSeeder.seed();
        transcriptItemSeeder.seed();

        achievementSeeder.seed();
        competencySeeder.seed();

        certificateSeeder.seed();
        credentialSeeder.seed();
        verificationSeeder.seed();

        institutionRequestSeeder.seed();
        invitationSeeder.seed();
    }
}
package com.cervalid.platform.academic.verification.service;

import com.cervalid.platform.academic.verification.dto.response.VerificationDashboardResponse;
import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import com.cervalid.platform.academic.verification.repository.VerificationRecordRepository;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationDashboardService {

    private final VerificationRecordRepository repository;
    private final SecurityContextService securityContextService;

    public VerificationDashboardResponse getDashboard() {

        Long institutionId =
                securityContextService.getInstitutionId();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = now.toLocalDate().atStartOfDay();
        LocalDateTime last7Days = now.minusDays(7);
        LocalDateTime last30Days = now.minusDays(30);

        long total =
                repository.countByInstitutionId(
                    institutionId);

        long certificates =
                repository.countDistinctByInstitutionIdAndCertificateIdNotNull(
                                institutionId);

        long valid =
                repository.countByInstitutionIdAndStatus(
                        institutionId,
                        VerificationStatus.VALID);

        long invalid =
                repository.countByInstitutionIdAndStatus(
                        institutionId,
                        VerificationStatus.INVALID);

        long revoked =
                repository.countByInstitutionIdAndStatus(
                        institutionId,
                        VerificationStatus.REVOKED);

        long notFound =
                repository.countByInstitutionIdAndStatus(
                        institutionId,
                        VerificationStatus.NOT_FOUND);

        long todayCount =
                repository.countByInstitutionIdAndVerifiedAtAfter(
                        institutionId,
                        today);

        long weekCount =
                repository.countByInstitutionIdAndVerifiedAtAfter(
                        institutionId,
                        last7Days);

        long monthCount =
                repository.countByInstitutionIdAndVerifiedAtAfter(
                        institutionId,
                        last30Days);

        double validRate =
                total == 0
                        ? 0
                        : ((double) valid * 100) / total;

        double invalidRate =
                total == 0
                        ? 0
                        : ((double) invalid * 100) / total;

        return VerificationDashboardResponse.builder()

                .totalVerifications(total)
                .uniqueCertificates(certificates)
                .today(todayCount)
                .last7Days(weekCount)
                .last30Days(monthCount)
                .validVerifications(valid)
                .invalidVerifications(invalid)
                .revokedVerifications(revoked)
                .notFoundVerifications(notFound)
                .validRate(Math.round(validRate * 10.0) / 10.0)
                .invalidRate(Math.round(invalidRate * 10.0) / 10.0)
                .build();
    }
}
package com.cervalid.platform.academic.verification.repository;

import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VerificationRecordRepository
        extends JpaRepository<VerificationRecord, Long>,
        JpaSpecificationExecutor<VerificationRecord> {

    Page<VerificationRecord> findByCertificateIdAndInstitutionIdOrderByVerifiedAtDesc(
            Long certificateId,
            Long institutionId,
            Pageable pageable);

    Page<VerificationRecord> findByInstitutionIdOrderByVerifiedAtDesc(
            Long institutionId,
            Pageable pageable);

    List<VerificationRecord> findByInstitutionIdAndStatus(
            Long institutionId,
            VerificationStatus status);

    Page<VerificationRecord> findByInstitutionId(
            Long institutionId,
            Pageable pageable);

    // PARA DASHBOARD
    long countByInstitutionId(
            Long institutionId);

    long countDistinctByInstitutionIdAndCertificateIdNotNull(
            Long institutionId);

    long countByInstitutionIdAndStatus(
            Long institutionId,
            VerificationStatus status);

    long countByInstitutionIdAndVerifiedAtAfter(
            Long institutionId,
            LocalDateTime verifiedAt);

    long countByInstitutionIdAndStatusAndVerifiedAtAfter(
            Long institutionId,
            VerificationStatus status,
            LocalDateTime verifiedAt);

    long countByCertificateId(
            Long certificateId);

    VerificationRecord
    findFirstByCertificateIdOrderByVerifiedAtDesc(
            Long certificateId);

    long countByCertificateIdAndStatus(
            Long certificateId,
            VerificationStatus status);

}

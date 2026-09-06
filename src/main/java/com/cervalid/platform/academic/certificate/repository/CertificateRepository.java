package com.cervalid.platform.academic.certificate.repository;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CertificateRepository
        extends JpaRepository<Certificate, Long>,
        JpaSpecificationExecutor<Certificate> {

    Optional<Certificate> findByPublicId(
            UUID publicId);

    Optional<Certificate> findByPublicIdAndInstitutionId(
            UUID publicId,
            Long institutionId
    );

    Optional<Certificate> findByCertificateNumber(
            String certificateNumber);

    Optional<Certificate> findByCertificateNumberAndInstitutionId(
            String certificateNumber,
            Long institutionId
    );

    Optional<Certificate> findByTranscriptId(
            Long transcriptId);

    boolean existsByCertificateNumber(
            String certificateNumber);

    boolean existsByTranscriptId(
            Long transcriptId);

    Page<Certificate> findByStudentIdAndInstitutionId(
            Long studentId,
            Long institutionId,
            Pageable pageable
    );

    // METODO PARA EL MODULO DE VERIFICATION
    Page<Certificate> findByInstitutionId(
            Long institutionId,
            Pageable pageable);

    Page<Certificate> findByInstitutionIdAndCertificateNumberContainingIgnoreCase(
            Long institutionId,
            String search,
            Pageable pageable);
}
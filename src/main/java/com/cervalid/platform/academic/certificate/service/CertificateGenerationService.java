package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.transcript.domain.TranscriptStatusManager;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificateGenerationService {

    private final CertificateRepository repository;
    private final TranscriptRepository transcriptRepository;
    private final TranscriptStatusManager transcriptStatusManager;
    private final CertificateHashService hashService;
    private final CertificateBlockchainService blockchainService;

    public Certificate generate(
            Certificate certificate,
            Transcript transcript) {

        hashService.generateHashes(certificate);
        Certificate saved = repository.save(certificate);
        transcriptStatusManager.issueTranscript(transcript);
        transcriptRepository.save(transcript);
        blockchainService.anchor(saved);
        return repository.save(saved);
    }
}
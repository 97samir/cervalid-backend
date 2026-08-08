package com.cervalid.platform.institution.service;

import com.cervalid.platform.institution.dto.AdminDashboardResponse;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.tenant.enums.RequestStatus;
import com.cervalid.platform.tenant.repository.InstitutionRequestRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final InstitutionRepository institutionRepository;
    private final InstitutionRequestRepository requestRepository;
    private final UserRepository userRepository;

    // dashboard para superadmin - tarjetas cards
    public AdminDashboardResponse getAdminStats() {

        long totalInstitutions = institutionRepository.count();
        long activeInstitutions = institutionRepository.countByActiveTrue();

        long pendingRequests =
                requestRepository.countByStatus(RequestStatus.PENDING);

        long totalUsers = userRepository.count();

        return AdminDashboardResponse.builder()
                .totalInstitutions(totalInstitutions)
                .activeInstitutions(activeInstitutions)
                .pendingRequests(pendingRequests)
                .totalUsers(totalUsers)
                .build();
    }
}

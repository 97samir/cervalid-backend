package com.cervalid.platform.institution.controller;

import com.cervalid.platform.institution.dto.AdminDashboardResponse;
import com.cervalid.platform.institution.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // muestra resumen para dashboard de superadmin
    @GetMapping("/dashboard-stats")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public AdminDashboardResponse getDashboard() {
        return adminService.getAdminStats();
    }
}

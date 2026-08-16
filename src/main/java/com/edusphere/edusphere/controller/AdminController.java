package com.edusphere.edusphere.controller;

import com.edusphere.edusphere.dto.response.AdminDashboardResponse;
import com.edusphere.edusphere.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponse> getDashboard() {
        return ResponseEntity.ok(
                adminService.getAdminDashboard()
        );
    }
}

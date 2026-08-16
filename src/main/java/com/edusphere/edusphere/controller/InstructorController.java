package com.edusphere.edusphere.controller;

import com.edusphere.edusphere.dto.response.InstructorDashboardResponse;
import com.edusphere.edusphere.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    @GetMapping("/me/dashboard")
    public ResponseEntity<InstructorDashboardResponse> getDashboard() {
        return ResponseEntity.ok(
                instructorService.getInstructorDashboard()
        );
    }
}

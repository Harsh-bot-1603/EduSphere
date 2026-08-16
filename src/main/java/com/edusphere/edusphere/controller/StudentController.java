package com.edusphere.edusphere.controller;

import com.edusphere.edusphere.dto.response.StudentDashBoardResponse;
import com.edusphere.edusphere.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/me/dashboard")
    public ResponseEntity<StudentDashBoardResponse> getDashboard() {
        return ResponseEntity.ok(
                studentService.getStudentDashboard()
        );
    }
}

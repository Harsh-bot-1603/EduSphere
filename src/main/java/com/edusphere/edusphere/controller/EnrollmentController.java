package com.edusphere.edusphere.controller;

import com.edusphere.edusphere.dto.response.EnrollmentResponse;
import com.edusphere.edusphere.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<EnrollmentResponse> enrollInCourse(@PathVariable Long courseId){
        EnrollmentResponse response = enrollmentService.enrollInCourse(courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/students/me/courses")
    public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments(){
        List<EnrollmentResponse> responses = enrollmentService.getMyEnrollments();
        return ResponseEntity.ok(responses);
    }
    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id){
        enrollmentService.unEnroll(id);
        return ResponseEntity.noContent().build();
    }
}

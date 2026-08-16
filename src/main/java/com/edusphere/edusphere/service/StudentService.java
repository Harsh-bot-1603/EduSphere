package com.edusphere.edusphere.service;

import com.edusphere.edusphere.dto.response.StudentDashBoardResponse;
import com.edusphere.edusphere.entity.Enrollment;
import com.edusphere.edusphere.entity.User;
import com.edusphere.edusphere.enums.EnrollmentStatus;
import com.edusphere.edusphere.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    private final EnrollmentRepository enrollmentRepository;
    public StudentDashBoardResponse getStudentDashboard() {
        User student = getCurrentUser();

        List<Enrollment> enrollments =
                enrollmentRepository.findByStudent(student);

        long totalCourses = enrollments.size();

        long completedCourses = enrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.COMPLETED)
                .count();

        long activeCourses = enrollments.stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.ACTIVE)
                .count();

        int overallProgress = 0;

        if (!enrollments.isEmpty()) {
            overallProgress = (int) enrollments.stream()
                    .mapToInt(Enrollment::getProgress)
                    .average()
                    .orElse(0);
        }

        return StudentDashBoardResponse.builder()
                .totalCourses(totalCourses)
                .completedCourses(completedCourses)
                .activeCourses(activeCourses)
                .overallProgress(overallProgress)
                .build();
    }
}

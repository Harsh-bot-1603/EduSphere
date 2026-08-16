package com.edusphere.edusphere.service;

import com.edusphere.edusphere.dto.response.InstructorDashboardResponse;
import com.edusphere.edusphere.entity.User;
import com.edusphere.edusphere.enums.RoleType;
import com.edusphere.edusphere.exception.UnauthorizedException;
import com.edusphere.edusphere.repository.CourseRepository;
import com.edusphere.edusphere.repository.EnrollmentRepository;
import com.edusphere.edusphere.repository.ReviewRepository;
import com.edusphere.edusphere.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstructorService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewRepository reviewRepository;
    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    public InstructorDashboardResponse getInstructorDashboard() {

        User instructor = getCurrentUser();

        if (instructor.getRole().getName() != RoleType.INSTRUCTOR) {
            throw new UnauthorizedException(
                    "Only instructors can access this dashboard."
            );
        }

        long totalCourses =
                courseRepository.findByTeacher(instructor).size();

        long totalStudents =
                enrollmentRepository.countDistinctStudentsByTeacher(instructor);

        long totalReviews =
                reviewRepository.countByTeacher(instructor);

        double averageRating =
                Optional.ofNullable(
                        reviewRepository.averageRatingByTeacher(instructor)
                ).orElse(0.0);

        return InstructorDashboardResponse.builder()
                .totalCourses(totalCourses)
                .totalStudents(totalStudents)
                .totalReviews(totalReviews)
                .averageRating(averageRating)
                .build();
    }
}

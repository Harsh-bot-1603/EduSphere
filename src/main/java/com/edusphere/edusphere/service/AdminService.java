package com.edusphere.edusphere.service;

import com.edusphere.edusphere.dto.response.AdminDashboardResponse;
import com.edusphere.edusphere.entity.User;
import com.edusphere.edusphere.enums.RoleType;
import com.edusphere.edusphere.exception.UnauthorizedException;
import com.edusphere.edusphere.repository.CourseRepository;
import com.edusphere.edusphere.repository.EnrollmentRepository;
import com.edusphere.edusphere.repository.ReviewRepository;
import com.edusphere.edusphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final EnrollmentRepository enrollmentRepository;
    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    public AdminDashboardResponse getAdminDashboard() {

        User admin = getCurrentUser();

        if (admin.getRole().getName() != RoleType.ADMIN) {
            throw new UnauthorizedException(
                    "Only admins can access this dashboard."
            );
        }

        long totalUsers = userRepository.count();
        long totalCourses = courseRepository.count();
        long totalEnrollments = enrollmentRepository.count();
        long totalReviews = reviewRepository.count();

        return AdminDashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalCourses(totalCourses)
                .totalEnrollments(totalEnrollments)
                .totalReviews(totalReviews)
                .build();
    }
}

package com.edusphere.edusphere.service;

import com.edusphere.edusphere.dto.response.EnrollmentResponse;
import com.edusphere.edusphere.entity.Course;
import com.edusphere.edusphere.entity.Enrollment;
import com.edusphere.edusphere.entity.User;
import com.edusphere.edusphere.enums.EnrollmentStatus;
import com.edusphere.edusphere.exception.CourseNotFoundException;
import com.edusphere.edusphere.exception.EnrollmentExistsException;
import com.edusphere.edusphere.exception.UnauthorisedEnrollmentException;
import com.edusphere.edusphere.repository.CourseRepository;
import com.edusphere.edusphere.repository.EnrollmentRepository;
import com.edusphere.edusphere.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private EnrollmentResponse mapToEnrollment(Enrollment enrollment){
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .CourseTitle(enrollment.getCourse().getTitle())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .progress(enrollment.getProgress())
                .status(enrollment.getStatus())
                .build();
    }
    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    public EnrollmentResponse enrollInCourse(Long courseId){
        User loggedInUser = getCurrentUser();
        if(loggedInUser.getRole().getName()!= RoleType.STUDENT) throw new UnauthorisedEnrollmentException(courseId);
        Course course = courseRepository.findById(courseId).orElseThrow(()-> new CourseNotFoundException(courseId));
        if(enrollmentRepository.existsByStudentAndCourse(loggedInUser,course)) throw new EnrollmentExistsException("You have already enrolled in this course.");
        Enrollment enrollment = Enrollment.builder()
                .course(course)
                .student(loggedInUser)
                .enrollmentDate(LocalDateTime.now())
                .status(EnrollmentStatus.ACTIVE)
                .progress(0)
                .build();
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return mapToEnrollment(savedEnrollment);
    }
    public List<EnrollmentResponse> getMyEnrollments(){
        User user = getCurrentUser();
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(user);
        List<EnrollmentResponse> responses = new ArrayList<>();
        for(Enrollment e : enrollments){
            responses.add(mapToEnrollment(e));
        }
        return responses;
    }
}

package com.edusphere.edusphere.service;

import com.edusphere.edusphere.dto.response.EnrollmentResponse;
import com.edusphere.edusphere.entity.*;
import com.edusphere.edusphere.enums.EnrollmentStatus;
import com.edusphere.edusphere.exception.*;
import com.edusphere.edusphere.repository.CourseRepository;
import com.edusphere.edusphere.repository.EnrollmentRepository;
import com.edusphere.edusphere.enums.RoleType;
import com.edusphere.edusphere.repository.LessonProgressRepository;
import com.edusphere.edusphere.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
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
    @Transactional
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
                .instructorName(course.getTeacher() != null
                        ? course.getTeacher().getName()
                        : null)
                .build();
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        List<Lesson> lessons = lessonRepository.findByCourseOrderByLessonOrder(course);
        List<LessonProgress> progresses = new ArrayList<>();
        for(Lesson l : lessons){
            progresses.add(LessonProgress.builder()
                    .lesson(l)
                    .enrollment(savedEnrollment)
                    .completed(false)
                    .build());

        }
        lessonProgressRepository.saveAll(progresses);
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
    public void unEnroll(Long id){
        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow(()-> new EnrollmentDoesnotExistsException("No Enrollment Found"));
        User loggedInUser = getCurrentUser();
        if(!enrollment.getStudent().getId().equals(loggedInUser.getId()) && loggedInUser.getRole().getName()!=RoleType.ADMIN)
            throw new UnauthorisedEnrollmentException(id);
        enrollmentRepository.delete(enrollment);

    }
}

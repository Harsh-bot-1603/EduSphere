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
                .courseTitle(enrollment.getCourse().getTitle())
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

        System.out.println("STEP 1 - Current user");
        User loggedInUser = getCurrentUser();

        System.out.println("STEP 2 - Role: " + loggedInUser.getRole().getName());

        if(loggedInUser.getRole().getName() != RoleType.STUDENT)
            throw new UnauthorisedEnrollmentException(courseId);

        System.out.println("STEP 3 - Finding course");

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        System.out.println("STEP 4 - Course found: " + course.getId());

        System.out.println("STEP 5 - Checking enrollment");

        if(enrollmentRepository.existsByStudentAndCourse(loggedInUser, course))
            throw new EnrollmentExistsException(
                    "You have already enrolled in this course."
            );

        System.out.println("STEP 6 - Creating enrollment");

        Enrollment enrollment = Enrollment.builder()
                .course(course)
                .student(loggedInUser)
                .enrollmentDate(LocalDateTime.now())
                .status(EnrollmentStatus.ACTIVE)
                .progress(0)
                .instructorName(
                        course.getTeacher() != null
                                ? course.getTeacher().getName()
                                : null
                )
                .build();

        System.out.println("STEP 7 - Saving enrollment");

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        System.out.println("STEP 8 - Enrollment saved: " + savedEnrollment.getId());

        List<Lesson> lessons =
                lessonRepository.findByCourseOrderByLessonOrder(course);

        System.out.println("STEP 9 - Lessons found: " + lessons.size());

        List<LessonProgress> progresses = new ArrayList<>();

        for(Lesson l : lessons){
            progresses.add(
                    LessonProgress.builder()
                            .lesson(l)
                            .enrollment(savedEnrollment)
                            .completed(false)
                            .build()
            );
        }

        System.out.println("STEP 10 - Saving lesson progress");

        lessonProgressRepository.saveAll(progresses);

        System.out.println("STEP 11 - Done");

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

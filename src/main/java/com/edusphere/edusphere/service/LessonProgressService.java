package com.edusphere.edusphere.service;

import com.edusphere.edusphere.entity.*;
import com.edusphere.edusphere.enums.EnrollmentStatus;
import com.edusphere.edusphere.exception.EnrollmentDoesnotExistsException;
import com.edusphere.edusphere.exception.LessonAlreadyCompletedException;
import com.edusphere.edusphere.exception.LessonNotFoundException;
import com.edusphere.edusphere.exception.LessonProgressNotFoundException;
import com.edusphere.edusphere.repository.EnrollmentRepository;
import com.edusphere.edusphere.repository.LessonProgressRepository;
import com.edusphere.edusphere.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonProgressService {
    private final LessonProgressRepository lessonProgressRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    @Transactional
    public void completeLesson(Long lessonId){
        User student = getCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(()->new LessonNotFoundException("lesson not found."));
        Course course = lesson.getCourse();
       Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(student,course).orElseThrow(()->new EnrollmentDoesnotExistsException("You are not enrolled."));
       LessonProgress lessonProgress =  lessonProgressRepository.findByEnrollmentAndLesson(enrollment,lesson).orElseThrow(()->new LessonProgressNotFoundException("Can not find any progress."));
       if(lessonProgress.isCompleted()) throw new LessonAlreadyCompletedException("You have completed the lesson.");
       lessonProgress.setCompleted(true);
       lessonProgress.setCompletedAt(LocalDateTime.now());
       lessonProgressRepository.save(lessonProgress);
       long completed = lessonProgressRepository.countByEnrollmentAndCompletedTrue(enrollment);
       int totalLessons = lessonRepository.findByCourseOrderByLessonOrder(course).size();
       int percentage = (int)(completed*100/totalLessons);
       enrollment.setProgress(percentage);
       if(percentage==100) enrollment.setStatus(EnrollmentStatus.COMPLETED);
       enrollmentRepository.save(enrollment);
    }
}

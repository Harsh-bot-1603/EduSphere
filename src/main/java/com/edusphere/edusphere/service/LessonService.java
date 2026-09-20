package com.edusphere.edusphere.service;

import com.edusphere.edusphere.dto.request.CreateLessonRequest;
import com.edusphere.edusphere.dto.request.UpdateLessonRequest;
import com.edusphere.edusphere.dto.response.LessonResponse;
import com.edusphere.edusphere.entity.Course;
import com.edusphere.edusphere.entity.Lesson;
import com.edusphere.edusphere.entity.User;
import com.edusphere.edusphere.enums.RoleType;
import com.edusphere.edusphere.exception.*;
import com.edusphere.edusphere.repository.CourseRepository;
import com.edusphere.edusphere.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private LessonResponse mapToLessonResponse(Lesson lesson){
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .lessonOrder(lesson.getLessonOrder())
                .durationInMinutes(lesson.getDurationInMinutes())
                .courseId(lesson.getCourse().getId())
                .videoUrl(lesson.getVideoUrl())
                .build();
    }
    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
    public LessonResponse createLesson(Long courseId, CreateLessonRequest request){
        User loggedInUser = getCurrentUser();
        Course course = courseRepository.findById(courseId).orElseThrow(()-> new CourseNotFoundException(courseId));
        if(loggedInUser.getRole().getName()!= RoleType.ADMIN && !course.getTeacher().getId().equals(loggedInUser.getId()))
            throw  new UnauthorisedLessonCreationException("Lesson cannot be created");
        if(lessonRepository.existsByCourseAndLessonOrder(course,request.getLessonOrder()))
            throw new LessonOrderAlreadyExistsException("Lesson order already exists");
        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .lessonOrder(request.getLessonOrder())
                .durationInMinutes(request.getDurationInMinutes())
                .videoUrl(request.getVideoUrl())
                .course(course)
                .build();
        Lesson savedLesson = lessonRepository.save(lesson);
        return mapToLessonResponse(savedLesson);

    }
    public List<LessonResponse> getLessonsByCourse(Long courseId){
        Course course = courseRepository.findById(courseId).orElseThrow(()->new CourseNotFoundException(courseId));
        List<Lesson> lessons = lessonRepository.findByCourseOrderByLessonOrder(course);
        List<LessonResponse> lessonResponses = new ArrayList<>();
        for(Lesson l : lessons){
            lessonResponses.add(mapToLessonResponse(l));
        }
        return lessonResponses;
    }

    public LessonResponse getLessonById(Long lessonId){
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(()-> new LessonNotFoundException("Lesson not found"));
        return mapToLessonResponse(lesson);
    }

    public LessonResponse updateLesson(Long lessonId,UpdateLessonRequest request){
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(()->new LessonNotFoundException("Lesson not found"));
        User loggedInUser = getCurrentUser();
        Course course = lesson.getCourse();
        if(loggedInUser.getRole().getName()!=RoleType.ADMIN && !course.getTeacher().getId().equals(loggedInUser.getId()))
            throw new UnauthorizedUpdateException("Cannot be updated");
        if(lesson.getLessonOrder()!= request.getLessonOrder()){
            if(lessonRepository.existsByCourseAndLessonOrder(course,request.getLessonOrder()))
                throw new LessonOrderAlreadyExistsException("Lesson order already exists");
        }
        lesson.setDescription(request.getDescription());
        lesson.setLessonOrder(request.getLessonOrder());
        lesson.setDurationInMinutes(request.getDurationInMinutes());
        lesson.setTitle(request.getTitle());
        lesson.setVideoUrl(request.getVideoUrl());
        Lesson savedlesson = lessonRepository.save(lesson);
        return mapToLessonResponse(savedlesson);
    }

    public void deleteLesson(Long lessonId){
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(()->new LessonNotFoundException("Lesson not found"));
        User loggedInUser = getCurrentUser();
        if(loggedInUser.getRole().getName()!=RoleType.ADMIN && !lesson.getCourse().getTeacher().getId().equals(loggedInUser.getId()))
            throw new UnauthorizedDeletionException("Cannot be deleted");
        lessonRepository.delete(lesson);
    }
}

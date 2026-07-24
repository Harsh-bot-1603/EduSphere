package com.edusphere.edusphere.service;

import com.edusphere.edusphere.dto.request.CreateCourseRequest;
import com.edusphere.edusphere.dto.response.CourseResponse;
import com.edusphere.edusphere.entity.Course;
import com.edusphere.edusphere.entity.User;
import com.edusphere.edusphere.enums.CourseStatus;
import com.edusphere.edusphere.exception.CourseNotFoundException;
import com.edusphere.edusphere.exception.UnauthorisedCourseCreationException;
import com.edusphere.edusphere.repository.CourseRepository;
import com.edusphere.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private CourseResponse mapToResponse(Course course){
        return   CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .price(course.getPrice())
                .description(course.getDescription())
                .teacherName(course.getTeacher().getName())
                .teacherId(course.getTeacher().getId())
                .status(course.getStatus())
                .build();
    }
    public CourseResponse createCourse(CreateCourseRequest request)  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Course course;
        if(user.getRole().getName() != RoleType.INSTRUCTOR) {
            throw new UnauthorisedCourseCreationException();
        }
        course = Course.builder()
                .description(request.getDescription())
                .title(request.getTitle())
                .price(request.getPrice())
                .teacher(user)
                .status(CourseStatus.DRAFT)
                .build();
        Course savedCourse = courseRepository.save(course);
        return mapToResponse(savedCourse);
    }
    public CourseResponse getCourseById(Long id){
        Course course = courseRepository.findById(id).orElseThrow(()-> new CourseNotFoundException(id));
        return mapToResponse(course);
    }
    public List<CourseResponse> getAllCourses(){
        List<Course> courses = courseRepository.findAll();
        List<CourseResponse> allCourses = new ArrayList<>();
        for(Course course : courses){
            allCourses.add(mapToResponse(course));
        }
        return allCourses;
    }
}

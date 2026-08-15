package com.edusphere.edusphere.service;

import com.edusphere.edusphere.dto.request.CreateCourseRequest;
import com.edusphere.edusphere.dto.request.UpdateCourseRequest;
import com.edusphere.edusphere.dto.response.CourseResponse;
import com.edusphere.edusphere.entity.Course;
import com.edusphere.edusphere.entity.User;
import com.edusphere.edusphere.enums.CourseStatus;
import com.edusphere.edusphere.exception.CourseNotFoundException;
import com.edusphere.edusphere.exception.UnauthorisedCourseCreationException;
import com.edusphere.edusphere.exception.UnauthorizedDeletionException;
import com.edusphere.edusphere.exception.UnauthorizedUpdateException;
import com.edusphere.edusphere.repository.CourseRepository;
import com.edusphere.edusphere.enums.RoleType;
import com.edusphere.edusphere.specification.CourseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
                .createdAt(LocalDateTime.now())
                .build();
        Course savedCourse = courseRepository.save(course);
        return mapToResponse(savedCourse);
    }
    public CourseResponse getCourseById(Long id){
        Course course = courseRepository.findById(id).orElseThrow(()-> new CourseNotFoundException(id));
        return mapToResponse(course);
    }
    public Page<CourseResponse> getAllCourses(Pageable pageable){
        Page<Course> courses = courseRepository.findAll(pageable);
        return courses.map(this::mapToResponse);
    }
    public CourseResponse updateCourse(Long id, UpdateCourseRequest request){
        Course course = courseRepository.findById(id).orElseThrow(()-> new CourseNotFoundException(id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();
        if(loggedInUser.getRole().getName()!=RoleType.ADMIN && !course.getTeacher().getId().equals(loggedInUser.getId()))
            throw new UnauthorizedUpdateException("You are not allowed to update this course");
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        Course savedCourse = courseRepository.save(course);
        return mapToResponse(savedCourse);
    }
    public void deleteCourse(Long id){
        Course course = courseRepository.findById(id).orElseThrow(()-> new CourseNotFoundException(id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();
        if(loggedInUser.getRole().getName()!=RoleType.ADMIN && !course.getTeacher().getId().equals(loggedInUser.getId()))
            throw new UnauthorizedDeletionException("You cannot delete the course");
        courseRepository.delete(course);
    }
    public Page<CourseResponse> searchCourse(String title, BigDecimal minPrice,BigDecimal maxPrice,Pageable pageable){
        Specification<Course> specification = (root,query,criteriaBuilder)->criteriaBuilder.conjunction();
        if(title!=null && !title.isBlank()) specification = specification.and(CourseSpecification.hasTitle(title));
        if(minPrice!=null) specification = specification.and(CourseSpecification.priceGreaterThanOrEqualTo(minPrice));
        if(maxPrice!=null) specification = specification.and(CourseSpecification.priceLessThanOrEqualTo(maxPrice));
        Page<Course> courses = courseRepository.findByTitleContainingIgnoreCase(title,pageable);
        return courses.map(this::mapToResponse);
    }
}

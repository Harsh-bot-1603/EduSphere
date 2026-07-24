package com.edusphere.edusphere.controller;

import com.edusphere.edusphere.dto.request.CreateCourseRequest;
import com.edusphere.edusphere.dto.response.CourseResponse;
import com.edusphere.edusphere.entity.Course;
import com.edusphere.edusphere.exception.CourseNotFoundException;
import com.edusphere.edusphere.repository.CourseRepository;
import com.edusphere.edusphere.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest request){
        CourseResponse savedCourse = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses(){
        List<CourseResponse> allCourses = courseService.getAllCourses();
        return ResponseEntity.ok(allCourses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id){
        CourseResponse response = courseService.getCourseById(id);
        return ResponseEntity.ok(response);
    }

}

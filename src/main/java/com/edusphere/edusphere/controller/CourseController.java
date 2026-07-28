package com.edusphere.edusphere.controller;

import com.edusphere.edusphere.dto.request.CreateCourseRequest;
import com.edusphere.edusphere.dto.request.UpdateCourseRequest;
import com.edusphere.edusphere.dto.response.CourseResponse;
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
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable Long id, @Valid @RequestBody UpdateCourseRequest request){
        CourseResponse response = courseService.updateCourse(id,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id){
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

}

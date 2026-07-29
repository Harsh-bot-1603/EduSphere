package com.edusphere.edusphere.controller;

import com.edusphere.edusphere.dto.request.CreateLessonRequest;
import com.edusphere.edusphere.dto.request.UpdateLessonRequest;
import com.edusphere.edusphere.dto.response.LessonResponse;
import com.edusphere.edusphere.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lessons")
public class LessonController {
    private final LessonService lessonService;
    @PostMapping("/courses/{courseId}")
    public ResponseEntity<LessonResponse> createLesson(@PathVariable Long courseId, @RequestBody @Valid CreateLessonRequest request){
        LessonResponse response = lessonService.createLesson(courseId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/courses/{courseId}/lessons")
    public ResponseEntity<List<LessonResponse>> getLessonByCourse(@PathVariable Long courseId){
        List<LessonResponse> responses = lessonService.getLessonsByCourse(courseId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/courses/{lessonId}")
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable Long lessonId){
        LessonResponse response = lessonService.getLessonById(lessonId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/courses/{lessonId}")
    public ResponseEntity<LessonResponse> updateLesson(@PathVariable Long lessonId, @RequestBody @Valid UpdateLessonRequest request){
        LessonResponse response = lessonService.updateLesson(lessonId,request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/courses/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId){
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }

}

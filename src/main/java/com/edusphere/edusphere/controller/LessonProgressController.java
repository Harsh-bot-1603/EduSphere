package com.edusphere.edusphere.controller;

import com.edusphere.edusphere.service.LessonProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/progress")
public class LessonProgressController {
    private final LessonProgressService lessonProgressService;
    @PostMapping("/{lessonId}/complete")
    public ResponseEntity<Void> completeLesson(@PathVariable Long lessonId){
        lessonProgressService.completeLesson(lessonId);
        return ResponseEntity.noContent().build();
    }
}

package com.edusphere.edusphere.exception;

public class LessonNotFoundException extends RuntimeException{
    public LessonNotFoundException(String lessonNotFound) {
        super(lessonNotFound);
    }
}

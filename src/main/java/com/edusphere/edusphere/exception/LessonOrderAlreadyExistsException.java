package com.edusphere.edusphere.exception;

public class LessonOrderAlreadyExistsException extends RuntimeException {
    public LessonOrderAlreadyExistsException(String lessonOrderAlreadyExists) {
        super(lessonOrderAlreadyExists);
    }
}

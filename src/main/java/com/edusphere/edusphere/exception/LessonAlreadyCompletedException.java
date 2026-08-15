package com.edusphere.edusphere.exception;

public class LessonAlreadyCompletedException extends RuntimeException {
    public LessonAlreadyCompletedException(String s) {
        super(s);
    }
}

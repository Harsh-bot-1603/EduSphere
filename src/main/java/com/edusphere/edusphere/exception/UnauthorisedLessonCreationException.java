package com.edusphere.edusphere.exception;

public class UnauthorisedLessonCreationException extends RuntimeException {
    public UnauthorisedLessonCreationException(String lessonCannotBeCreated) {
        super(lessonCannotBeCreated);
    }
}

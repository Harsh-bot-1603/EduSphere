package com.edusphere.edusphere.exception;

public class UnauthorizedDeletionException extends RuntimeException {
    public UnauthorizedDeletionException(String youCannotDeleteTheCourse) {
        super(youCannotDeleteTheCourse);
    }
}

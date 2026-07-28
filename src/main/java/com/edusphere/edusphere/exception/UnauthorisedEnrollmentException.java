package com.edusphere.edusphere.exception;

public class UnauthorisedEnrollmentException extends RuntimeException {
    public UnauthorisedEnrollmentException(Long id){
        super("Course does not exists");
    }
}

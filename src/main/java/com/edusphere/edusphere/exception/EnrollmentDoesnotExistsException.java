package com.edusphere.edusphere.exception;

public class EnrollmentDoesnotExistsException extends RuntimeException {
    public EnrollmentDoesnotExistsException(String noEnrollmentFound) {
        super(noEnrollmentFound);
    }
}

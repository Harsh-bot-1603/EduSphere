package com.edusphere.edusphere.exception;

import com.edusphere.enums.RoleType;

public class RoleAlreadyExistsException extends RuntimeException {
    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}

package com.edusphere.edusphere.exception;


import com.edusphere.enums.RoleType;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String message){
        super(message);
    }
}

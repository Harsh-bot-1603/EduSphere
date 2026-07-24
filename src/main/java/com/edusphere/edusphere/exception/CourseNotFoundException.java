package com.edusphere.edusphere.exception;

public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(Long id){
        super("course with id "+ id +" not found");
    }

}

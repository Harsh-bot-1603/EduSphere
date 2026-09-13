package com.edusphere.edusphere.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRoleAlreadyExists(RoleAlreadyExistsException ex, HttpServletRequest request){

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request){

        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(),error.getDefaultMessage()));


        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .message("Validation Failed")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFound(RoleNotFoundException ex, HttpServletRequest request){

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .message("Role not found")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request){

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .message("Invalid request body")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request){

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .message("Parameter '" + ex.getName() + "' must be of type " +
                        ex.getRequiredType().getSimpleName())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request){

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getRequestURI())
                .message("No resource found")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request){

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getRequestURI())
                .message(ex.getMessage())
                .build();
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        500,
                        "Internal Server Error",
                        request.getRequestURI(),
                        ex.getMessage(),
                        null
                ));
    }


}

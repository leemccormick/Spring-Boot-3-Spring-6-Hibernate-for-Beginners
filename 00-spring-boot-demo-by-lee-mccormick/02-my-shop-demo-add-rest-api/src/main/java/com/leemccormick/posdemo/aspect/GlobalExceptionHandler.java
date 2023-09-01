package com.leemccormick.posdemo.aspect;

import com.leemccormick.posdemo.entity.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("error"); // error.html
        modelAndView.addObject("errorMessage", "An error occurred: " + e.getMessage());
        log.error(String.format("ERROR : GlobalExceptionHandler | ModelAndView | return error.html --> %s", e.getMessage()));
        return modelAndView;
    }

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<?> handleApiErrorException(ApiErrorException ex) {
        ApiResponse response = new ApiResponse(true, ex.getHttpStatus().value(), ex.getMessage());
        log.error(String.format("ERROR : GlobalExceptionHandler | handleApiErrorException | return api.errorResponse --> %s", ex.getMessage()));
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse response = new ApiResponse(true, status.value(), ex.getMessage());
        log.error(String.format("ERROR : GlobalExceptionHandler | handleMissingServletRequestParameterException | return api.errorResponse --> %s", ex.getMessage()));
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse response = new ApiResponse(true, status.value(), ex.getMessage());
        log.error(String.format("ERROR : GlobalExceptionHandler | handleMethodArgumentTypeMismatchException | return api.errorResponse --> %s", ex.getMessage()));
        return ResponseEntity.status(status).body(response);
    }
}
package com.leemccormick.demo.rest;

// FOR ERROR HANDEL EXCEPTION...
/*
- 1) Create a custom error response class --> StudentErrorResponse
- 2) Create a custom exception class --> StudentNoFoundException
- 3) Update REST service to throw exception if student not found --> StudentRestController
- 4) Add an exception handler using @ExceptionHandler --> StudentRestController
*/
public class StudentErrorResponse {
    private int status;
    private String message;
    private long timeStamp;

    public StudentErrorResponse() {

    }

    public StudentErrorResponse(String message, long timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

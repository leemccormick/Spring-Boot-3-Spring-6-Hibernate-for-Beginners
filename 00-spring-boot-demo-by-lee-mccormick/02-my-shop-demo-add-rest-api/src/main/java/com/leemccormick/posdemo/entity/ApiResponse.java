package com.leemccormick.posdemo.entity;

public class ApiResponse {
    private String status;
    private int code;
    private String message;

    public ApiResponse(boolean isError, int code, String errorMessage) {
        if (isError) {
            this.status = "error";
            this.code = code;
            this.message = errorMessage;
        } else {
            status = "success";
            this.code = 200;
            this.message = null;
        }
    }

    public ApiResponse(boolean isError) {
        if (isError) {
            this.status = "error";
            this.code = 500;
            this.message = "Unknown error, something went wrong.";
        } else {
            status = "success";
            this.code = 200;
            this.message = null;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}

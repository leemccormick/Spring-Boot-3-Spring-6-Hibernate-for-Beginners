package com.leemccormick.posdemo.entity;

import java.util.ArrayList;

public class ErrorResponse {
    private boolean hasError;
    private ArrayList<String> errorMessages;

    public ErrorResponse() {

    }

    public ErrorResponse(boolean hasError, ArrayList<String> errorMessages) {
        this.hasError = hasError;
        this.errorMessages = errorMessages;
    }

    public boolean getHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(ArrayList<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public void addErrorMessage(String error) {
        if (errorMessages == null) {
            errorMessages = new ArrayList<>();
        }
        errorMessages.add(error);
    }
}

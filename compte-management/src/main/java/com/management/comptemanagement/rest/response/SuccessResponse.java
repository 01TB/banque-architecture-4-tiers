package com.management.comptemanagement.rest.response;

public class SuccessResponse {
    private String message;
    private boolean success = true;

    public SuccessResponse(String message) {
        this.message = message;
    }

    // Getters et Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
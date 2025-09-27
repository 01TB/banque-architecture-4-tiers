package com.management.comptemanagement.rest.response;

public class ErrorResponse {
    private String error;
    private boolean success = false;

    public ErrorResponse(String error) {
        this.error = error;
    }

    // Getters et Setters
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
package com.espe.drivex.dto;

import java.util.List;

public class AuthResponse {

    private String email;
    private List<String> authorities;
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(String email, List<String> authorities, String message) {
        this.email = email;
        this.authorities = authorities;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
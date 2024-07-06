package com.aicodinghelper.core.service.response;

public class AuthResponse {
    private String authToken;

    public AuthResponse(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}

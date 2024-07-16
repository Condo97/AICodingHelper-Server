package com.aicodinghelper.core.service.request;

public class AuthRequest {
    private String authToken;
    private String openAIKey;

    public AuthRequest() {

    }

    public AuthRequest(String authToken, String openAIKey) {
        this.authToken = authToken;
        this.openAIKey = openAIKey;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getOpenAIKey() {
        return openAIKey;
    }

}

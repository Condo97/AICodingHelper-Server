package com.aicodinghelper.core.service.request;

public class FeedbackRequest extends AuthRequest {

    private String feedback;

    public FeedbackRequest() {

    }

    public FeedbackRequest(String authToken, String openAIKey, String feedback) {
        super(authToken, openAIKey);
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

}

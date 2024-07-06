package com.aicodinghelper.core.service.request;

import com.oaigptconnector.model.generation.OpenAIGPTModels;

public class CalculateTokensRequest extends AuthRequest {

    private OpenAIGPTModels model;
    private String input;

    public CalculateTokensRequest() {

    }

    public CalculateTokensRequest(String authToken, OpenAIGPTModels model, String input) {
        super(authToken);
        this.model = model;
        this.input = input;
    }

    public OpenAIGPTModels getModel() {
        return model;
    }

    public String getInput() {
        return input;
    }

}
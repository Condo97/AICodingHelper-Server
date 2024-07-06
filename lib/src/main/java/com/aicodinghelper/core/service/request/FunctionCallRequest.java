package com.aicodinghelper.core.service.request;

import com.oaigptconnector.model.generation.OpenAIGPTModels;

public class FunctionCallRequest extends AuthRequest {

    private OpenAIGPTModels model;
    private String systemMessage;
    private String input;

    public FunctionCallRequest() {

    }

    public FunctionCallRequest(String authToken, OpenAIGPTModels model, String systemMessage, String input) {
        super(authToken);
        this.model = model;
        this.systemMessage = systemMessage;
        this.input = input;
    }

    public OpenAIGPTModels getModel() {
        return model;
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    public String getInput() {
        return input;
    }

}

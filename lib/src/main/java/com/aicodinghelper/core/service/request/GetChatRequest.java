package com.aicodinghelper.core.service.request;

import com.aicodinghelper.database.model.Sender;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oaigptconnector.model.generation.OpenAIGPTModels;
import com.oaigptconnector.model.request.chat.completion.OAIChatCompletionRequest;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetChatRequest extends AuthRequest {

    private OAIChatCompletionRequest chatCompletionRequest;
//    private OpenAIGPTModels model;

    public GetChatRequest() {

    }

    public GetChatRequest(String authToken, OAIChatCompletionRequest chatCompletionRequest) {
        super(authToken);
        this.chatCompletionRequest = chatCompletionRequest;
//        this.model = model;
    }

    public OAIChatCompletionRequest getChatCompletionRequest() {
        return chatCompletionRequest;
    }

//    public OpenAIGPTModels getModel() {
//        return model;
//    }

}

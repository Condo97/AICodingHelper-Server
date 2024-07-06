package com.aicodinghelper.core.service.response;

import com.oaigptconnector.model.response.chat.completion.stream.OpenAIGPTChatCompletionStreamResponse;

public class GetChatStreamResponse {

    private OpenAIGPTChatCompletionStreamResponse oaiResponse;

    public GetChatStreamResponse() {

    }

    public GetChatStreamResponse(OpenAIGPTChatCompletionStreamResponse oaiResponse) {
        this.oaiResponse = oaiResponse;
    }

    public OpenAIGPTChatCompletionStreamResponse getOaiResponse() {
        return oaiResponse;
    }

}

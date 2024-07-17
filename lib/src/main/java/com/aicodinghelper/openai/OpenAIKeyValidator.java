package com.aicodinghelper.openai;

import com.aicodinghelper.Constants;
import com.oaigptconnector.model.OAIChatCompletionRequestMessageBuilder;
import com.oaigptconnector.model.OAIClient;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.oaigptconnector.model.generation.OpenAIGPTModels;
import com.oaigptconnector.model.request.chat.completion.*;
import com.oaigptconnector.model.response.chat.completion.http.OAIGPTChatCompletionResponse;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;

public class OpenAIKeyValidator {

    private static final String invalidAPIKeyOpenAIGPTErrorCode = "invalid_api_key"; // TODO: Move this to a constant for gpt error codes

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofMinutes(com.oaigptconnector.Constants.AI_TIMEOUT_MINUTES)).build();

    public static boolean validate(String openAIKey) throws OpenAIGPTException, IOException, InterruptedException {
        // Create chat completion request
        OAIChatCompletionRequest chatCompletionRequest = OAIChatCompletionRequest.build(
                OpenAIGPTModels.GPT_4_TURBO.getName(),
                1,
                Constants.DEFAULT_TEMPERATURE,
                false,
                new OAIChatCompletionRequestResponseFormat(ResponseFormatType.TEXT),
                new OAIChatCompletionRequestStreamOptions(true),
                new OAIChatCompletionRequestMessageBuilder(CompletionRole.USER).addText("A").build()
        );

        // Return true if successful or false if request throws an OpenAIGPTException and has invalidAPIKeyOpenAIGPTErrorCode as the error response code
        boolean openAIKeyIsValid = false;
        try {
            // Get response from OAIClient
            OAIGPTChatCompletionResponse response = OAIClient.postChatCompletion(
                    chatCompletionRequest,
                    openAIKey,
                    httpClient
            );

            // Return true
            return true;
        } catch (OpenAIGPTException e) {
            if (e.getErrorObject().getError().getCode().equals(invalidAPIKeyOpenAIGPTErrorCode)) {
                // Return false
                return false;
            } else {
                // Throw e
                throw e;
            }
        }
    }

}

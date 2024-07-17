package com.aicodinghelper.core.service.endpoints;

import com.aicodinghelper.core.service.request.AuthRequest;
import com.aicodinghelper.core.service.response.ValidateOpenAITokenResponse;
import com.aicodinghelper.openai.OpenAIKeyValidator;
import com.oaigptconnector.model.exception.OpenAIGPTException;

import java.io.IOException;

public class ValidateOpenAITokenEndpoint {

    public static ValidateOpenAITokenResponse validateOpenAIToken(AuthRequest request) throws OpenAIGPTException, IOException, InterruptedException {
        // Validate openAIKey
        boolean openAIKeyIsValid = OpenAIKeyValidator.validate(request.getOpenAIKey());

        // Return ValidateOpenAITokenResponse with openAIKeyIsValid
        return new ValidateOpenAITokenResponse(openAIKeyIsValid);
    }

}

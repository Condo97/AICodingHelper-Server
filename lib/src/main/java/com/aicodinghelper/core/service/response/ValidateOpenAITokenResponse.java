package com.aicodinghelper.core.service.response;

public class ValidateOpenAITokenResponse {

    private Boolean isValid;

    public ValidateOpenAITokenResponse() {

    }

    public ValidateOpenAITokenResponse(Boolean isValid) {
        this.isValid = isValid;
    }

    public Boolean getValid() {
        return isValid;
    }

}

package com.aicodinghelper.core.service.response;

public class GetRemainingTokensResponse {
    private Long remainingTokens;

    public GetRemainingTokensResponse() {

    }

    public GetRemainingTokensResponse(long remainingTokens) {
        this.remainingTokens = remainingTokens;
    }

    public Long getRemainingTokens() {
        return remainingTokens;
    }

    public void setRemainingTokens(long remainingTokens) {
        this.remainingTokens = remainingTokens;
    }
}

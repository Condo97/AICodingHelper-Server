package com.aicodinghelper.core.service.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.aicodinghelper.core.service.ResponseStatus;

public class StatusResponse {

    private ResponseStatus status;

    public StatusResponse() {

    }

    public StatusResponse(ResponseStatus status) {
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    // Is this too hacky?
    @JsonProperty(value = "Success")
    public int getSuccess() {
        return status.Success;
    }

}

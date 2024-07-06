package com.aicodinghelper.core.service.response.factory;

import com.aicodinghelper.core.service.ResponseStatus;
import com.aicodinghelper.core.service.response.StatusResponse;

public class StatusResponseFactory {

    public static StatusResponse createSuccessStatusResponse() {
        return createStatusResponse(ResponseStatus.SUCCESS);
    }

    public static StatusResponse createStatusResponse(ResponseStatus status) {
        return new StatusResponse(status);
    }

}

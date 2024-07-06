package com.aicodinghelper.core.service.response.factory;

import com.aicodinghelper.core.service.ResponseStatus;
import com.aicodinghelper.core.service.response.BodyResponse;

public class BodyResponseFactory {

    public static BodyResponse createSuccessBodyResponse(Object object) {
        return createBodyResponse(ResponseStatus.SUCCESS, object);
    }

    public static BodyResponse createBodyResponse(ResponseStatus status, Object object) {
        return new BodyResponse(status, object);
    }

}

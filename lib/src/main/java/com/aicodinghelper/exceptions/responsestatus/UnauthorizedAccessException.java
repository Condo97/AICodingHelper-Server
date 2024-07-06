package com.aicodinghelper.exceptions.responsestatus;

import com.aicodinghelper.exceptions.ResponseStatusException;
import com.aicodinghelper.core.service.ResponseStatus;

public class UnauthorizedAccessException extends ResponseStatusException {

    private final ResponseStatus responseStatus = ResponseStatus.UNAUTHORIZED_ACCESS;

    public UnauthorizedAccessException(String responseMessage) {
        super(responseMessage);
    }

    public UnauthorizedAccessException(String message, String responseMessage) {
        super(message, responseMessage);
    }

    public UnauthorizedAccessException(String message, Throwable cause, String responseMessage) {
        super(message, cause, responseMessage);
    }

    public UnauthorizedAccessException(Throwable cause, String responseMessage) {
        super(cause, responseMessage);
    }

    @Override
    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

}

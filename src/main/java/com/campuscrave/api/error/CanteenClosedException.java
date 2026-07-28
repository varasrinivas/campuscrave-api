package com.campuscrave.api.error;

import org.springframework.http.HttpStatus;

public class CanteenClosedException extends ApiException {

    public CanteenClosedException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

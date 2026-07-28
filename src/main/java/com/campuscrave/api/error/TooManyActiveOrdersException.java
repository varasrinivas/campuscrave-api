package com.campuscrave.api.error;

import org.springframework.http.HttpStatus;

public class TooManyActiveOrdersException extends ApiException {

    public TooManyActiveOrdersException(int max) {
        super(HttpStatus.CONFLICT, "You already have " + max + " orders in flight. Collect one first.");
    }
}

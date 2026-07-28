package com.campuscrave.api.error;

import org.springframework.http.HttpStatus;

/** A failure we planned for, with the status the browser should see. */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

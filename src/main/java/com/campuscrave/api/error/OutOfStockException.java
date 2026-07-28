package com.campuscrave.api.error;

import org.springframework.http.HttpStatus;

public class OutOfStockException extends ApiException {

    public OutOfStockException(String dishName) {
        super(HttpStatus.CONFLICT, dishName + " just sold out");
    }
}

package com.campuscrave.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Stands in for the campus payment gateway, which does not exist yet.
 * Every top-up is approved. No money moves anywhere.
 */
@Service
public class PaymentMockService {

    private static final Logger log = LoggerFactory.getLogger(PaymentMockService.class);

    private final String apiSecret;

    public PaymentMockService(@Value("${campuscrave.payments.api-secret}") String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public void authorise(Long studentId, int amountRupees) {
        log.info("Authorising top-up of Rs.{} for student {} with key {}", amountRupees, studentId, apiSecret);
    }
}

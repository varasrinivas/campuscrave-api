package com.campuscrave.api.service;

import com.campuscrave.api.config.BusinessRules;
import com.campuscrave.api.entity.CanteenConfig;
import com.campuscrave.api.repository.CanteenConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Optional;

/**
 * Reads the canteen's own settings, falling back to {@link BusinessRules} defaults
 * if nobody has configured anything yet.
 */
@Service
@Transactional(readOnly = true)
public class CanteenConfigService {

    private final CanteenConfigRepository repository;

    public CanteenConfigService(CanteenConfigRepository repository) {
        this.repository = repository;
    }

    private Optional<CanteenConfig> current() {
        return repository.findById(1L);
    }

    public LocalTime orderCutoff() {
        return current().map(CanteenConfig::getOrderCutoff).orElse(BusinessRules.ORDER_CUTOFF);
    }

    public LocalTime rushStart() {
        return current().map(CanteenConfig::getRushStart).orElse(BusinessRules.RUSH_WINDOW_START);
    }

    public LocalTime rushEnd() {
        return current().map(CanteenConfig::getRushEnd).orElse(BusinessRules.RUSH_WINDOW_END);
    }

    public int maxActiveOrders() {
        return current().map(CanteenConfig::getMaxActiveOrders).orElse(BusinessRules.MAX_ACTIVE_ORDERS);
    }

    public boolean isAcceptingOrders() {
        return current().map(CanteenConfig::isAcceptingOrders).orElse(true);
    }
}

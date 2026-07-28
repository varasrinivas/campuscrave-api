package com.campuscrave.api.service;

import com.campuscrave.api.config.BusinessRules;
import com.campuscrave.api.dto.RushMeterDto;
import com.campuscrave.api.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Powers the little red dot on the menu screen that tells students whether
 * the window is mobbed right now.
 */
@Service
@Transactional(readOnly = true)
public class RushMeterService {

    private static final ZoneId CAMPUS_ZONE = ZoneId.of("Asia/Kolkata");
    private static final int BASE_WAIT_MINUTES = 6;

    private final OrderRepository orderRepository;
    private final CanteenConfigService canteenConfig;

    public RushMeterService(OrderRepository orderRepository, CanteenConfigService canteenConfig) {
        this.orderRepository = orderRepository;
        this.canteenConfig = canteenConfig;
    }

    public RushMeterDto current() {
        LocalTime nowOnCampus = LocalTime.now(CAMPUS_ZONE);
        LocalTime start = canteenConfig.rushStart();
        LocalTime end = canteenConfig.rushEnd();

        boolean rushOn = !nowOnCampus.isBefore(start) && !nowOnCampus.isAfter(end);
        long recentOrders = orderRepository.countPlacedSince(Instant.now().minus(Duration.ofHours(1)));

        int wait = BASE_WAIT_MINUTES + (int) (recentOrders / 4);
        if (rushOn) {
            wait += BusinessRules.RUSH_WAIT_PENALTY_MINUTES;
        }

        return new RushMeterDto(rushOn, recentOrders, wait, start, end);
    }
}

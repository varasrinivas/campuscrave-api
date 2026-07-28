package com.campuscrave.api.dto;

import com.campuscrave.api.entity.OrderStatus;

import java.time.Instant;
import java.util.List;

public record OrderStatusDto(
        Long orderId,
        int tokenNumber,
        OrderStatus status,
        int totalRupees,
        String pickupBlock,
        Instant createdAt,
        List<LineDto> items
) {
    public record LineDto(String dishName, int quantity, int unitPriceRupees) {
    }
}

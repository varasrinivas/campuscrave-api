package com.campuscrave.api.dto;

import com.campuscrave.api.entity.OrderStatus;

public record OrderResponse(
        Long orderId,
        int tokenNumber,
        OrderStatus status,
        int totalRupees,
        String pickupBlock,
        int remainingStock
) {
}

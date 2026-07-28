package com.campuscrave.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * What the browser posts when a student taps "Place order".
 *
 * @param totalRupees the cart total the web app already worked out and showed
 *                    the student, so the two never disagree on screen
 */
public record PlaceOrderRequest(
        @NotNull Long studentId,
        String pickupBlock,
        @Min(0) int totalRupees,
        @NotEmpty @Valid List<Line> items
) {
    public record Line(@NotNull Long dishId, @Min(1) int quantity) {
    }
}

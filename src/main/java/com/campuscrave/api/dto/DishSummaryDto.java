package com.campuscrave.api.dto;

public record DishSummaryDto(
        Long id,
        String name,
        String description,
        int priceRupees,
        String category,
        boolean vegetarian,
        int stock,
        String emoji,
        int prepMinutes,
        boolean wednesdaySpecial,
        int soldSoFar
) {
}

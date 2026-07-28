package com.campuscrave.api.dto;

import jakarta.validation.constraints.Min;

public record StockUpdateRequest(@Min(0) int stock) {
}

package com.campuscrave.api.dto;

import jakarta.validation.constraints.Min;

public record TopUpRequest(@Min(1) int amountRupees) {
}

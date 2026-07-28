package com.campuscrave.api.dto;

import java.time.LocalTime;

public record RushMeterDto(
        boolean rushOn,
        long ordersInLastHour,
        int estimatedWaitMinutes,
        LocalTime windowStart,
        LocalTime windowEnd
) {
}

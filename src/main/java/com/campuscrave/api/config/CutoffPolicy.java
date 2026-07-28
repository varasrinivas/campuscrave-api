package com.campuscrave.api.config;

import com.campuscrave.api.service.CanteenConfigService;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * Decides whether the kitchen is still taking orders.
 *
 * <p>The cutoff stored in {@code canteen_config} is written in IST, because that is
 * what Anna Madam types in. The server clock is not guaranteed to be IST, so we shift
 * the cutoff onto the server's clock before comparing it with {@link LocalTime#now()}.
 */
@Component
public class CutoffPolicy {

    /** India Standard Time runs this far ahead of UTC. */
    private static final int IST_OFFSET_HOURS = 5;
    private static final int IST_OFFSET_MINUTES = 30;

    private final CanteenConfigService canteenConfig;

    public CutoffPolicy(CanteenConfigService canteenConfig) {
        this.canteenConfig = canteenConfig;
    }

    /**
     * The configured IST cutoff, expressed on the server's own clock.
     */
    LocalTime cutoffOnServerClock() {
        return canteenConfig.orderCutoff()
                .minusHours(IST_OFFSET_HOURS)
                .minusMinutes(IST_OFFSET_MINUTES);
    }

    /**
     * @return true once the canteen has stopped taking orders for today
     */
    public boolean isPastCutoff() {
        return LocalTime.now().isAfter(cutoffOnServerClock());
    }
}

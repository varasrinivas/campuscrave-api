package com.campuscrave.api.config;

import java.time.LocalTime;

/**
 * The canteen's rules, in one place, with the names everyone uses out loud.
 *
 * <p>These are the defaults. Anna Madam can override them in the
 * {@code canteen_config} table without waiting for a deploy.
 */
public final class BusinessRules {

    /** A student may have at most this many orders in flight at once. */
    public static final int MAX_ACTIVE_ORDERS = 3;

    /** No orders after this. IST. */
    public static final LocalTime ORDER_CUTOFF = LocalTime.of(14, 30);

    /** When the queue at the window gets real. IST. */
    public static final LocalTime RUSH_WINDOW_START = LocalTime.of(12, 15);
    public static final LocalTime RUSH_WINDOW_END = LocalTime.of(13, 45);

    /** Minutes added to the quoted wait once the rush is on. */
    public static final int RUSH_WAIT_PENALTY_MINUTES = 6;

    private BusinessRules() {
    }
}

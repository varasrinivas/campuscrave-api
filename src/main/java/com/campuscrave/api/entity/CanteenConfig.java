package com.campuscrave.api.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

/**
 * One row. The canteen's own settings, editable by Anna Madam without a redeploy.
 *
 * <p>All times in this table are <b>IST</b> — the canteen has never thought about
 * any other timezone, and neither has this column.
 */
@Entity
@Table(name = "canteen_config")
public class CanteenConfig {

    @Id
    private Long id;

    /** Last moment an order can be placed for today. Canon: 14:30 IST. */
    @Column(name = "order_cutoff", nullable = false)
    private LocalTime orderCutoff;

    /** Canon: 12:15 IST. */
    @Column(name = "rush_start", nullable = false)
    private LocalTime rushStart;

    /** Canon: 13:45 IST. */
    @Column(name = "rush_end", nullable = false)
    private LocalTime rushEnd;

    @Column(name = "max_active_orders", nullable = false)
    private int maxActiveOrders;

    @Column(name = "accepting_orders", nullable = false)
    private boolean acceptingOrders = true;

    protected CanteenConfig() {
    }

    public Long getId() {
        return id;
    }

    public LocalTime getOrderCutoff() {
        return orderCutoff;
    }

    public LocalTime getRushStart() {
        return rushStart;
    }

    public LocalTime getRushEnd() {
        return rushEnd;
    }

    public int getMaxActiveOrders() {
        return maxActiveOrders;
    }

    public boolean isAcceptingOrders() {
        return acceptingOrders;
    }
}

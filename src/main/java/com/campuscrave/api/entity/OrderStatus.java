package com.campuscrave.api.entity;

/**
 * The life of an order, as the canteen sees it.
 *
 * <p>The status name travels to the browser exactly as spelled here — the web app
 * switches on the raw string.
 */
public enum OrderStatus {

    /** Student tapped "Place order". Money is held, the canteen hasn't looked yet. */
    PLACED,

    /** Anna Madam accepted it at the counter. */
    ACCEPTED,

    /** On the stove. */
    COOKING,

    /** Sitting at the Block C window, waiting for a hungry human. */
    READY,

    /** Picked up. Done. */
    COLLECTED,

    /** Cancelled by the student before it was cooked. */
    CANCELLED;

    public boolean isActive() {
        return this != COLLECTED && this != CANCELLED;
    }

    public boolean isCancellable() {
        return this == PLACED || this == ACCEPTED;
    }
}

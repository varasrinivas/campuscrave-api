package com.campuscrave.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @Column(nullable = false)
    private int quantity;

    /** Price captured at order time — the menu can change tomorrow. */
    @Column(name = "unit_price_rupees", nullable = false)
    private int unitPriceRupees;

    protected OrderItem() {
    }

    public OrderItem(Dish dish, int quantity) {
        this.dish = dish;
        this.quantity = quantity;
        this.unitPriceRupees = dish.getPriceRupees();
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    void setOrder(Order order) {
        this.order = order;
    }

    public Dish getDish() {
        return dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitPriceRupees() {
        return unitPriceRupees;
    }

    public int lineTotalRupees() {
        return unitPriceRupees * quantity;
    }
}

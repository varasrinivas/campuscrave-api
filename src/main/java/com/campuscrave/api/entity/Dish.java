package com.campuscrave.api.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 300)
    private String description;

    /** Whole rupees. The canteen has never charged paise and never will. */
    @Column(name = "price_rupees", nullable = false)
    private int priceRupees;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private boolean vegetarian;

    /** Portions left today. Reset by the kitchen every morning. */
    @Column(nullable = false)
    private int stock;

    private String emoji;

    @Column(name = "prep_minutes", nullable = false)
    private int prepMinutes;

    @Column(name = "wednesday_special", nullable = false)
    private boolean wednesdaySpecial;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    protected Dish() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPriceRupees() {
        return priceRupees;
    }

    public String getCategory() {
        return category;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getEmoji() {
        return emoji;
    }

    public int getPrepMinutes() {
        return prepMinutes;
    }

    public boolean isWednesdaySpecial() {
        return wednesdaySpecial;
    }

    public boolean isActive() {
        return active;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}

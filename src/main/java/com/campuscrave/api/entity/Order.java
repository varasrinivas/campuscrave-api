package com.campuscrave.api.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PLACED;

    @Column(name = "total_rupees", nullable = false)
    private int totalRupees;

    /** The canteen token — what the student actually shouts at the window. */
    @Column(name = "token_number", nullable = false)
    private int tokenNumber;

    @Column(name = "pickup_block")
    private String pickupBlock;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected Order() {
    }

    public Order(Student student, int tokenNumber, String pickupBlock) {
        this.student = student;
        this.tokenNumber = tokenNumber;
        this.pickupBlock = pickupBlock;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getTotalRupees() {
        return totalRupees;
    }

    public void setTotalRupees(int totalRupees) {
        this.totalRupees = totalRupees;
    }

    public int getTokenNumber() {
        return tokenNumber;
    }

    public String getPickupBlock() {
        return pickupBlock;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}

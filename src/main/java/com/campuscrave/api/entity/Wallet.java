package com.campuscrave.api.entity;

import jakarta.persistence.*;

/**
 * The campus wallet. Parents top it up, students spend it on biryani.
 * There is no real money here — see {@code PaymentMockService}.
 */
@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private Student student;

    @Column(name = "balance_rupees", nullable = false)
    private int balanceRupees;

    protected Wallet() {
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public int getBalanceRupees() {
        return balanceRupees;
    }

    public void debit(int rupees) {
        if (rupees > balanceRupees) {
            throw new IllegalStateException("Wallet balance too low");
        }
        this.balanceRupees -= rupees;
    }

    public void credit(int rupees) {
        this.balanceRupees += rupees;
    }
}

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

    /** Money moves through WalletRepository.debit / credit — one guarded statement each. */
    public int getBalanceRupees() {
        return balanceRupees;
    }
}

package com.campuscrave.api.service;

import com.campuscrave.api.dto.WalletDto;
import com.campuscrave.api.entity.Wallet;
import com.campuscrave.api.error.NotFoundException;
import com.campuscrave.api.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final PaymentMockService payments;

    public WalletService(WalletRepository walletRepository, PaymentMockService payments) {
        this.walletRepository = walletRepository;
        this.payments = payments;
    }

    @Transactional(readOnly = true)
    public WalletDto balance(Long studentId) {
        Wallet wallet = load(studentId);
        return new WalletDto(studentId, wallet.getBalanceRupees());
    }

    @Transactional
    public WalletDto topUp(Long studentId, int amountRupees) {
        Wallet wallet = load(studentId);
        payments.authorise(studentId, amountRupees);
        wallet.credit(amountRupees);
        walletRepository.save(wallet);
        return new WalletDto(studentId, wallet.getBalanceRupees());
    }

    @Transactional
    public void debit(Long studentId, int amountRupees) {
        Wallet wallet = load(studentId);
        wallet.debit(amountRupees);
        walletRepository.save(wallet);
    }

    @Transactional
    public void refund(Long studentId, int amountRupees) {
        Wallet wallet = load(studentId);
        wallet.credit(amountRupees);
        walletRepository.save(wallet);
    }

    private Wallet load(Long studentId) {
        return walletRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NotFoundException("No wallet for student " + studentId));
    }
}

package com.campuscrave.api.repository;

import com.campuscrave.api.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByStudentId(Long studentId);
}

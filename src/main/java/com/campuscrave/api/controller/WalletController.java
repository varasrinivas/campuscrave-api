package com.campuscrave.api.controller;

import com.campuscrave.api.dto.TopUpRequest;
import com.campuscrave.api.dto.WalletDto;
import com.campuscrave.api.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{studentId}")
    public WalletDto balance(@PathVariable Long studentId) {
        return walletService.balance(studentId);
    }

    @PostMapping("/{studentId}/topup")
    public WalletDto topUp(@PathVariable Long studentId, @Valid @RequestBody TopUpRequest request) {
        return walletService.topUp(studentId, request.amountRupees());
    }
}

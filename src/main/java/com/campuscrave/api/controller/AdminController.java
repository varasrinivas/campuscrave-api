package com.campuscrave.api.controller;

import com.campuscrave.api.dto.DishSummaryDto;
import com.campuscrave.api.dto.StockUpdateRequest;
import com.campuscrave.api.service.AdminService;
import com.campuscrave.api.service.LegacyOrderHelper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Anna Madam's counter tablet. No authentication yet — the tablet never
 * leaves the counter, which is the whole security model for now.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final LegacyOrderHelper legacyOrderHelper;

    public AdminController(AdminService adminService, LegacyOrderHelper legacyOrderHelper) {
        this.adminService = adminService;
        this.legacyOrderHelper = legacyOrderHelper;
    }

    @PutMapping("/dishes/{id}/stock")
    public DishSummaryDto updateStock(@PathVariable Long id, @Valid @RequestBody StockUpdateRequest request) {
        return adminService.updateStock(id, request.stock());
    }

    @GetMapping("/summary")
    public Map<String, Object> daySummary(@RequestParam(required = false) Long studentId) {
        return legacyOrderHelper.buildDaySummary(studentId);
    }
}

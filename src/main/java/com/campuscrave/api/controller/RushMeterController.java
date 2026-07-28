package com.campuscrave.api.controller;

import com.campuscrave.api.dto.RushMeterDto;
import com.campuscrave.api.service.RushMeterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rush")
public class RushMeterController {

    private final RushMeterService rushMeterService;

    public RushMeterController(RushMeterService rushMeterService) {
        this.rushMeterService = rushMeterService;
    }

    @GetMapping
    public RushMeterDto current() {
        return rushMeterService.current();
    }
}

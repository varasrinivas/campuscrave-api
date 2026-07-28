package com.campuscrave.api.controller;

import com.campuscrave.api.dto.DishSummaryDto;
import com.campuscrave.api.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<DishSummaryDto> list() {
        return menuService.listMenu();
    }

    @GetMapping("/{id}")
    public DishSummaryDto detail(@PathVariable Long id) {
        return menuService.getDish(id);
    }
}

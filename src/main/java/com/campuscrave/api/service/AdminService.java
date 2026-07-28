package com.campuscrave.api.service;

import com.campuscrave.api.dto.DishSummaryDto;
import com.campuscrave.api.entity.Dish;
import com.campuscrave.api.error.NotFoundException;
import com.campuscrave.api.repository.DishRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * What the counter tablet is allowed to change.
 */
@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final DishRepository dishRepository;
    private final MenuService menuService;

    public AdminService(DishRepository dishRepository, MenuService menuService) {
        this.dishRepository = dishRepository;
        this.menuService = menuService;
    }

    /**
     * Kitchen cooked a fresh batch — set the shelf count to what is actually there.
     */
    @Transactional
    public DishSummaryDto updateStock(Long dishId, int stock) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new NotFoundException("No dish with id " + dishId));

        int before = dish.getStock();
        dish.setStock(stock);
        dishRepository.save(dish);

        log.info("Stock for {} set from {} to {}", dish.getName(), before, stock);
        return menuService.getDish(dishId);
    }
}

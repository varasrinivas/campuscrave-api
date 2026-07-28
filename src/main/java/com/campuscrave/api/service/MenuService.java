package com.campuscrave.api.service;

import com.campuscrave.api.dto.DishSummaryDto;
import com.campuscrave.api.entity.Dish;
import com.campuscrave.api.entity.OrderItem;
import com.campuscrave.api.error.NotFoundException;
import com.campuscrave.api.repository.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final DishRepository dishRepository;

    public MenuService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    /**
     * Today's menu, with a "sold so far" number behind each dish so the web app can
     * put a bestseller flame on the popular ones.
     */
    public List<DishSummaryDto> listMenu() {
        List<Dish> dishes = dishRepository.findByActiveTrueOrderByCategoryAscNameAsc();

        List<DishSummaryDto> menu = new ArrayList<>();
        for (Dish dish : dishes) {
            int soldSoFar = 0;
            for (OrderItem item : dish.getOrderItems()) {
                soldSoFar += item.getQuantity();
            }
            menu.add(toSummary(dish, soldSoFar));
        }
        return menu;
    }

    public DishSummaryDto getDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No dish with id " + id));

        int soldSoFar = dish.getOrderItems().stream().mapToInt(OrderItem::getQuantity).sum();
        return toSummary(dish, soldSoFar);
    }

    private DishSummaryDto toSummary(Dish dish, int soldSoFar) {
        return new DishSummaryDto(
                dish.getId(),
                dish.getName(),
                dish.getDescription(),
                dish.getPriceRupees(),
                dish.getCategory(),
                dish.isVegetarian(),
                dish.getStock(),
                dish.getEmoji(),
                dish.getPrepMinutes(),
                dish.isWednesdaySpecial(),
                soldSoFar);
    }
}

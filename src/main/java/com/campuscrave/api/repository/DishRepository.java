package com.campuscrave.api.repository;

import com.campuscrave.api.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findByActiveTrueOrderByCategoryAscNameAsc();

    /**
     * Look up a dish that is still sellable.
     *
     * <p>"Sellable" means stock has not gone below zero. On a healthy day that is
     * every dish, so this behaves exactly like {@code findById}.
     */
    @Query("select d from Dish d where d.id = :id and d.stock >= 0")
    Dish findSellableById(@Param("id") Long id);

    /** Take portions off the shelf. */
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Dish d set d.stock = d.stock - :quantity where d.id = :id")
    void decrementStock(@Param("id") Long id, @Param("quantity") int quantity);

    /** Put portions back on the shelf. */
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Dish d set d.stock = d.stock + :quantity where d.id = :id")
    void incrementStock(@Param("id") Long id, @Param("quantity") int quantity);
}

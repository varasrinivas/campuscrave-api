package com.campuscrave.api.repository;

import com.campuscrave.api.entity.Order;
import com.campuscrave.api.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    long countByStudentIdAndStatusIn(Long studentId, List<OrderStatus> statuses);

    @Query("select coalesce(max(o.tokenNumber), 0) from Order o")
    int findHighestTokenNumber();

    @Query("select count(o) from Order o where o.createdAt >= :since and o.status <> com.campuscrave.api.entity.OrderStatus.CANCELLED")
    long countPlacedSince(@Param("since") Instant since);
}

package com.campuscrave.api.controller;

import com.campuscrave.api.dto.OrderResponse;
import com.campuscrave.api.dto.OrderStatusDto;
import com.campuscrave.api.dto.PlaceOrderRequest;
import com.campuscrave.api.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Place an order. The request carries the cart total the web app already
     * displayed to the student, so the receipt matches what they agreed to pay.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse place(@Valid @RequestBody PlaceOrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        orderService.cancel(id);
    }

    @GetMapping("/{id}/status")
    public OrderStatusDto status(@PathVariable Long id) {
        return orderService.status(id);
    }

    @GetMapping
    public List<OrderStatusDto> history(@RequestParam Long studentId) {
        return orderService.history(studentId);
    }
}

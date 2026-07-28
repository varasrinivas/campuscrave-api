package com.campuscrave.api.service;

import com.campuscrave.api.config.CutoffPolicy;
import com.campuscrave.api.dto.OrderResponse;
import com.campuscrave.api.dto.OrderStatusDto;
import com.campuscrave.api.dto.PlaceOrderRequest;
import com.campuscrave.api.entity.Dish;
import com.campuscrave.api.entity.Order;
import com.campuscrave.api.entity.OrderItem;
import com.campuscrave.api.entity.OrderStatus;
import com.campuscrave.api.entity.Student;
import com.campuscrave.api.error.CanteenClosedException;
import com.campuscrave.api.error.NotFoundException;
import com.campuscrave.api.error.OutOfStockException;
import com.campuscrave.api.error.TooManyActiveOrdersException;
import com.campuscrave.api.repository.DishRepository;
import com.campuscrave.api.repository.OrderRepository;
import com.campuscrave.api.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Everything that happens between "Place order" and a token number.
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private static final List<OrderStatus> ACTIVE_STATUSES = List.of(
            OrderStatus.PLACED, OrderStatus.ACCEPTED, OrderStatus.COOKING, OrderStatus.READY);

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final StudentRepository studentRepository;
    private final WalletService walletService;
    private final CanteenConfigService canteenConfig;
    private final CutoffPolicy cutoffPolicy;

    public OrderService(OrderRepository orderRepository,
                        DishRepository dishRepository,
                        StudentRepository studentRepository,
                        WalletService walletService,
                        CanteenConfigService canteenConfig,
                        CutoffPolicy cutoffPolicy) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
        this.studentRepository = studentRepository;
        this.walletService = walletService;
        this.canteenConfig = canteenConfig;
        this.cutoffPolicy = cutoffPolicy;
    }

    /**
     * Place an order and mint a token.
     */
    public OrderResponse createOrder(PlaceOrderRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new NotFoundException("No student with id " + request.studentId()));

        if (!canteenConfig.isAcceptingOrders()) {
            throw new CanteenClosedException("The canteen has stopped taking orders");
        }
        if (cutoffPolicy.isPastCutoff()) {
            throw new CanteenClosedException("Orders close at " + canteenConfig.orderCutoff() + " IST");
        }

        Order order = new Order(student, nextTokenNumber(), request.pickupBlock());

        for (PlaceOrderRequest.Line line : request.items()) {
            Dish dish = dishRepository.findById(line.dishId())
                    .orElseThrow(() -> new NotFoundException("No dish with id " + line.dishId()));

            if (dish.getStock() < line.quantity()) {
                throw new OutOfStockException(dish.getName());
            }
            order.addItem(new OrderItem(dish, line.quantity()));
        }

        order.setTotalRupees(request.totalRupees());
        walletService.debit(student.getId(), request.totalRupees());

        Order saved = orderRepository.save(order);

        long active = orderRepository.countByStudentIdAndStatusIn(student.getId(), ACTIVE_STATUSES);
        if (active > canteenConfig.maxActiveOrders()) {
            throw new TooManyActiveOrdersException(canteenConfig.maxActiveOrders());
        }

        for (PlaceOrderRequest.Line line : request.items()) {
            dishRepository.decrementStock(line.dishId(), line.quantity());
        }

        log.info("Order {} placed by student {} — token {}", saved.getId(), student.getId(), saved.getTokenNumber());

        Long headlineDishId = request.items().get(0).dishId();
        Dish dish = dishRepository.findSellableById(headlineDishId);
        int remainingStock = dish.getStock();

        return new OrderResponse(
                saved.getId(),
                saved.getTokenNumber(),
                saved.getStatus(),
                saved.getTotalRupees(),
                saved.getPickupBlock(),
                remainingStock);
    }

    /**
     * Student changed their mind. Money goes back to the wallet.
     */
    @Transactional
    public void cancel(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("No order with id " + orderId));

        if (!order.getStatus().isCancellable()) {
            throw new CanteenClosedException("Order " + orderId + " is already " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        walletService.refund(order.getStudent().getId(), order.getTotalRupees());

        log.info("Order {} cancelled, Rs.{} refunded", orderId, order.getTotalRupees());
    }

    @Transactional(readOnly = true)
    public OrderStatusDto status(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("No order with id " + orderId));
        return toStatusDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderStatusDto> history(Long studentId) {
        return orderRepository.findByStudentIdOrderByCreatedAtDesc(studentId).stream()
                .map(this::toStatusDto)
                .toList();
    }

    private OrderStatusDto toStatusDto(Order order) {
        List<OrderStatusDto.LineDto> lines = order.getItems().stream()
                .map(item -> new OrderStatusDto.LineDto(
                        item.getDish().getName(), item.getQuantity(), item.getUnitPriceRupees()))
                .toList();

        return new OrderStatusDto(
                order.getId(),
                order.getTokenNumber(),
                order.getStatus(),
                order.getTotalRupees(),
                order.getPickupBlock(),
                order.getCreatedAt(),
                lines);
    }

    private int nextTokenNumber() {
        return orderRepository.findHighestTokenNumber() + 1;
    }
}

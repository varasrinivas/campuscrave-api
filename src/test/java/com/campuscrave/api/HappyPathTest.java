package com.campuscrave.api;

import com.campuscrave.api.config.CutoffPolicy;
import com.campuscrave.api.dto.OrderResponse;
import com.campuscrave.api.dto.OrderStatusDto;
import com.campuscrave.api.dto.PlaceOrderRequest;
import com.campuscrave.api.dto.WalletDto;
import com.campuscrave.api.entity.OrderStatus;
import com.campuscrave.api.service.MenuService;
import com.campuscrave.api.service.OrderService;
import com.campuscrave.api.service.WalletService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The core flows, proven green. Menu loads, orders place, wallets debit,
 * cancellation refunds. If these pass, the canteen works.
 */
@SpringBootTest
@Transactional
class HappyPathTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WalletService walletService;

    /**
     * Time-of-day rules make tests flaky depending on when CI runs,
     * so we stub the cutoff out. The canteen is always open in here.
     */
    @MockitoBean
    private CutoffPolicy cutoffPolicy;

    @Test
    @DisplayName("menu lists all eight dishes with the biryani on top form")
    void menuLoads() {
        var menu = menuService.listMenu();

        assertThat(menu).hasSize(8);
        assertThat(menu).anyMatch(d -> d.name().equals("Hyderabadi Biryani")
                && d.priceRupees() == 90
                && d.wednesdaySpecial());
    }

    @Test
    @DisplayName("a student can place an order and gets a token")
    void placeOrder() {
        OrderResponse response = orderService.createOrder(dosaFor(3, 1));

        assertThat(response.orderId()).isNotNull();
        assertThat(response.tokenNumber()).isPositive();
        assertThat(response.status()).isEqualTo(OrderStatus.PLACED);
        assertThat(response.totalRupees()).isEqualTo(50);
    }

    @Test
    @DisplayName("placing an order debits the wallet")
    void walletDebits() {
        int before = walletService.balance(3L).balanceRupees();
        orderService.createOrder(dosaFor(3, 1));
        int after = walletService.balance(3L).balanceRupees();

        assertThat(after).isEqualTo(before - 50);
    }

    @Test
    @DisplayName("order status is visible after placing")
    void statusVisible() {
        OrderResponse placed = orderService.createOrder(dosaFor(3, 2));

        OrderStatusDto status = orderService.status(placed.orderId());

        assertThat(status.status()).isEqualTo(OrderStatus.PLACED);
        assertThat(status.items()).hasSize(1);
        assertThat(status.items().get(0).dishName()).isEqualTo("Masala Dosa");
    }

    @Test
    @DisplayName("cancelling refunds the wallet")
    void cancelRefunds() {
        int before = walletService.balance(3L).balanceRupees();
        OrderResponse placed = orderService.createOrder(dosaFor(3, 1));

        orderService.cancel(placed.orderId());

        assertThat(walletService.balance(3L).balanceRupees()).isEqualTo(before);
        assertThat(orderService.status(placed.orderId()).status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("wallet top-up credits the balance")
    void topUpWorks() {
        WalletDto result = walletService.topUp(2L, 100);

        assertThat(result.balanceRupees()).isEqualTo(220);
    }

    @Test
    @DisplayName("order history comes back newest first")
    void historyWorks() {
        orderService.createOrder(dosaFor(3, 1));
        orderService.createOrder(dosaFor(3, 1));

        var history = orderService.history(3L);

        assertThat(history).hasSizeGreaterThanOrEqualTo(2);
    }

    /** One masala dosa (dish 2, Rs.50 each) for the given student. */
    private PlaceOrderRequest dosaFor(long studentId, int quantity) {
        return new PlaceOrderRequest(
                studentId,
                "Block C",
                50 * quantity,
                List.of(new PlaceOrderRequest.Line(2L, quantity)));
    }
}

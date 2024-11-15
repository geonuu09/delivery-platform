package com.example.delivery.order.service;

import com.example.delivery.order.dto.request.OrderCreateRequestDto;
import com.example.delivery.order.dto.response.OrderListResponseDto;
import com.example.delivery.order.dto.response.OrderResponseDto;
import com.example.delivery.user.entity.User;
import com.example.delivery.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.UUID;

import static com.example.delivery.order.entity.Order.OrderStatus.RECEIVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceIntegrationTest {

    @Autowired
    OrderGetService orderService;
    @Autowired
    UserRepository userRepository;

    User user;
    OrderResponseDto createdOrder = null;
    String updatedOrderStatus = null;

    @Test
    @Order(1)
    @DisplayName("주문 접수")
    void test1() {
        // given
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        Boolean isDelivery = true;
        String deliveryStreetAddress = "특별시 중구 세종대로 15";
        String deliveryDetailAddress = "1101동 101호";
        String requirements = "문 앞에 두고 벨 눌러 주세요";
        int totalCount = 2;
        int totalPrice = 10000;
        com.example.delivery.order.entity.Order.OrderStatus orderStatus = RECEIVED;

        OrderCreateRequestDto requestDto = new OrderCreateRequestDto(
                storeId,
                isDelivery,
                deliveryStreetAddress,
                deliveryDetailAddress,
                requirements,
                totalCount,
                totalPrice,
                orderStatus
        );

        //Owner 유저
        user = userRepository.findById(2L).orElse(null);

        // when
        OrderResponseDto order = orderService.createOrderByOwner(user, requestDto);

        // then
        assertNotNull(order.getOrderId());
        assertEquals(deliveryStreetAddress, order.getDeliveryStreetAddress());
        assertEquals(deliveryDetailAddress, order.getDeliveryDetailAddress());
        assertEquals(requirements, order.getRequirements());
        assertEquals(2, order.getTotalCount());
        assertEquals(10000, order.getTotalPrice());
        assertEquals("접수중", order.getOrderStatus());
        createdOrder = order;
    }

    @Test
    @Order(2)
    @DisplayName("주문 상태 변경")
    void test2() {
        // given
        Long userId = user.getUserId();
        UUID orderId = this.createdOrder.getOrderId();
        String orderStatus = "PREPARING";

        // when
        OrderResponseDto order = orderService.updateOrderStatus(userId, orderId, orderStatus);

        // then
        assertEquals(this.createdOrder.getOrderId(), order.getOrderId());
        assertEquals(this.createdOrder.getDeliveryStreetAddress(), order.getDeliveryStreetAddress());
        assertEquals(this.createdOrder.getDeliveryDetailAddress(), order.getDeliveryDetailAddress());
        assertEquals(this.createdOrder.getRequirements(), order.getRequirements());
        assertEquals(this.createdOrder.getTotalCount(), order.getTotalCount());
        assertEquals(this.createdOrder.getTotalPrice(), order.getTotalPrice());
        assertEquals("준비중", order.getOrderStatus());

        this.updatedOrderStatus = com.example.delivery.order.entity.Order.OrderStatus.PREPARING.getLabel();
    }

    @Test
    @Order(3)
    @DisplayName("주문 목록 조회")
    void test3() {
        // given
        Long userId = user.getUserId();

        // when
        Page<OrderListResponseDto> ordertList = orderService.getOrderListByOwner(userId, 0, 10, "orderId", false);

        // then
        // 1. 전체 주문에서 테스트에 의해 생성된 주문 찾기
        UUID createdOrderId = this.createdOrder.getOrderId();
        OrderListResponseDto foundOrder = ordertList.stream()
                .filter(oreder -> oreder.getOrderId().equals(createdOrderId))
                .findFirst()
                .orElse(null);

        // 2. 1번 테스트에 의해 접수된 주문과 일치하는지 검증
        assertEquals(this.createdOrder.getOrderId(), foundOrder.getOrderId());
        assertEquals(this.createdOrder.getDeliveryStreetAddress(), foundOrder.getDeliveryStreetAddress());
        assertEquals(this.createdOrder.getDeliveryDetailAddress(), foundOrder.getDeliveryDetailAddress());
        assertEquals(this.createdOrder.getTotalCount(), foundOrder.getTotalCount());
        assertEquals(this.createdOrder.getTotalPrice(), foundOrder.getTotalPrice());

        // 3. 2번 테스트에 의해 주문상태가 정상적으로 업데이트되었는지 검증
        assertEquals(this.updatedOrderStatus, foundOrder.getOrderStatus());
    }
}
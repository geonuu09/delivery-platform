package com.example.delivery.order.dto.response;

import com.example.delivery.cart.dto.response.CartResponseDto;
import com.example.delivery.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class OrderDetailResponseDto {
    private UUID orderId;
    private String storeName;
    private String orderStatus;
    private List<CartResponseDto> orderMenus;
    private int totalCount;
    private int totalPrice;
    private String deliveryStreetAddress;
    private String deliveryDetailAddress;
    private String requirements;

    public static OrderDetailResponseDto from(Order order) {
        List<CartResponseDto> orderMenus = order.getCarts().stream()
                .map(CartResponseDto::from)
                .collect(Collectors.toList());
        return OrderDetailResponseDto.builder()
                .orderId(order.getOrderId())
                .storeName(order.getStore().getStoreName())
                .orderStatus(order.getOrderStatus().getLabel())
                .orderMenus(orderMenus)
                .totalCount(order.getTotalCount())
                .totalPrice(order.getTotalPrice())
                .deliveryStreetAddress(order.getDeliveryStreetAddress())
                .deliveryDetailAddress(order.getDeliveryDetailAddress())
                .requirements(order.getRequirements())
                .build();
    }
}


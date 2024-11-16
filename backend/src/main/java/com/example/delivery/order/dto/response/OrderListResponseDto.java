package com.example.delivery.order.dto.response;

import com.example.delivery.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponseDto {
    private UUID orderId;
    private String storeName;
    private String deliveryStreetAddress;
    private String deliveryDetailAddress;
    private int totalCount;
    private int totalPrice;
    private String orderStatus;
    private LocalDateTime createdAt;

    public static OrderListResponseDto from(Order order) {
        return OrderListResponseDto.builder()
                .orderId(order.getOrderId())
                .storeName(order.getStore().getStoreName())
                .deliveryStreetAddress(order.getDeliveryStreetAddress())
                .deliveryDetailAddress(order.getDeliveryDetailAddress())
                .totalCount(order.getTotalCount())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus().getLabel())
                .createdAt(order.getCreatedAt())
                .build();
    }
}

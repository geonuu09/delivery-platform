package com.example.delivery.order.dto.response;

import com.example.delivery.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderDetailResponseDto {
    private UUID orderId;
    private String storeName;
    private String orderStatus;
//    private List<CartResponseDto> orderMenus;
    private int totalCount;
    private int totalPrice;
    private String dStreetAddress;
    private String dDetailAddress;
    private String requirements;

    public static OrderDetailResponseDto from(Order order) {
        return OrderDetailResponseDto.builder()
                .orderId(order.getOrderId())
                .storeName(order.getStore().getStoreName())
                .orderStatus(order.getOrderStatus().getLabel())
                //.orderMenus(CartResponseDto.from(order.getCarts()))
                .totalCount(order.getTotalCount())
                .totalPrice(order.getTotalPrice())
                .dStreetAddress(order.getDStreetAddress())
                .dDetailAddress(order.getDDetailAddress())
                .requirements(order.getRequirements())
                .build();
    }
}


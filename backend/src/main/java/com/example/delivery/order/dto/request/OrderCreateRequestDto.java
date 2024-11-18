package com.example.delivery.order.dto.request;

import com.example.delivery.order.entity.Order;
import com.example.delivery.store.entity.Store;
import com.example.delivery.user.entity.User;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDto {
    private UUID storeId;

    private Boolean isDelivery;

    private String deliveryStreetAddress;

    private String deliveryDetailAddress;

    private String requirements;

    private int totalCount;
    private int totalPrice;
    private Order.OrderStatus orderStatus;

    public Order toEntity(User user, Store store) {
        return Order.builder()
                .user(user)
                .store(store)
                .isDelivery(this.isDelivery)
                .deliveryStreetAddress(this.deliveryStreetAddress)
                .deliveryDetailAddress(this.deliveryDetailAddress)
                .requirements(this.requirements)
                .totalCount(this.totalCount)
                .totalPrice(this.totalPrice)
                .orderStatus(Order.OrderStatus.RECEIVED)
                .build();
    }
}

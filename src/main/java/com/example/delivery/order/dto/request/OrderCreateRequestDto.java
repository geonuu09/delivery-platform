package com.example.delivery.order.dto.request;

import com.example.delivery.order.entity.Order;
import com.example.delivery.store.entity.Store;
import com.example.delivery.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDto {
    private UUID storeId;

    @NotNull
    private Boolean isDelivery;

    @NotBlank(message = "배달 주소 입력은 필수입니다.")
    private String dStreetAddress;

    @NotBlank(message = "상세 주소는 필수입니다.")
    private String dDetailAddress;

    private String requirements;
    private int totalCount;
    private int totalPrice;
    private Order.OrderStatus orderStatus;

    public Order toEntity(User user, Store store) {
        return Order.builder()
                .user(user)
                .store(store)
                .isDelivery(this.isDelivery)
                .dStreetAddress(this.dStreetAddress)
                .dDetailAddress(this.dDetailAddress)
                .requirements(this.requirements)
                .totalCount(this.totalCount)
                .totalPrice(this.totalPrice)
                .orderStatus(this.orderStatus)
                .build();
    }
}

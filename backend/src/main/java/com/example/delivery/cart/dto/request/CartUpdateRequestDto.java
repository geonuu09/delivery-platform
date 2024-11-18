package com.example.delivery.cart.dto.request;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartUpdateRequestDto {
    private UUID cartId;
    private UUID menuId;
    private List<UUID> menuOptionIdList;
    private int menuCount;
}

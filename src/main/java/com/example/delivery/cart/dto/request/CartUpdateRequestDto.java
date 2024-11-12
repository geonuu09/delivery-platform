package com.example.delivery.cart.dto.request;

import com.example.delivery.menu.entity.MenuOption;
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
    private List<MenuOption> menuOptionList;
    private int menuCount;
    private int menuPrice;
}

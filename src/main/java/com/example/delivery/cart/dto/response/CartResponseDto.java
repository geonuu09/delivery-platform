package com.example.delivery.cart.dto.response;

import com.example.delivery.cart.entity.Cart;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
    private UUID cartId;
    private UUID menuId;
    private String menuName;
    private List<CartMenuOptionResponseDto> menuOptions;
    private int menuCount;
    private int menuPrice;
    //private String cartStatus;

    public static CartResponseDto from(Cart cart) {
        List<CartMenuOptionResponseDto> menuOptions = cart.getMenuOptions().stream()
                .map(CartMenuOptionResponseDto::from)
                .collect(Collectors.toList());
        return CartResponseDto.builder()
                .cartId(cart.getCartId())
                .menuId(cart.getMenu().getMenuId())
                .menuName(cart.getMenu().getMenuName())
                .menuOptions(menuOptions)
                .menuCount(cart.getCount())
                .menuPrice(cart.getPrice())
                //.cartStatus(cart.getCartStatus().getLabel())
                .build();
    }
}

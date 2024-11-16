package com.example.delivery.cart.dto.response;

import com.example.delivery.menu.entity.MenuOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartMenuOptionResponseDto {
    private UUID optionId;
    private String optionName;
    private int optionPrice;

    public static CartMenuOptionResponseDto from(MenuOption MenuOption) {
        return CartMenuOptionResponseDto.builder()
                .optionId(MenuOption.getMenuOptionId())
                .optionName(MenuOption.getOptionName())
                .optionPrice(MenuOption.getOptionPrice())
                .build();
    }
}

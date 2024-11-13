package com.example.delivery.menu.dto;

import com.example.delivery.menu.entity.MenuOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuOptionResponseDto {
    private UUID optionId;
    private String optionName;
    private int optionPrice;

    public MenuOptionResponseDto(MenuOption menuOption) {
        this.optionId = menuOption.getMenuOptionId();
        this.optionName = menuOption.getOptionName();
        this.optionPrice = menuOption.getOptionPrice();
    }
}
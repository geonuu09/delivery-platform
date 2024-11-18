package com.example.delivery.menu.dto;

import com.example.delivery.menu.entity.MenuOption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class MenuOptionResponseDto {

    @Schema(description = "메뉴 옵션 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID optionId;

    @Schema(description = "옵션 이름", example = "치즈 추가")
    private String optionName;

    @Schema(description = "옵션 가격", example = "500")
    private int optionPrice;

    public MenuOptionResponseDto(MenuOption menuOption) {
        this.optionId = menuOption.getMenuOptionId();
        this.optionName = menuOption.getOptionName();
        this.optionPrice = menuOption.getOptionPrice();
    }
}
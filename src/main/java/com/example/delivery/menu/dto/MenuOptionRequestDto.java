package com.example.delivery.menu.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.util.UUID;

@Getter
public class MenuOptionRequestDto {

    private UUID menuOptionId;

    private UUID menuId;

    @NotBlank(message = "옵션 이름은 필수입니다.")
    private String optionName;

    @Positive(message = "옵션 가격은 0보다 커야 합니다.")
    private int optionPrice;

    private boolean deleted;

    public MenuOption toEntity(Menu menu) {
        return MenuOption.builder()
                .optionName(this.optionName)
                .optionPrice(this.optionPrice)
                .deleted(this.deleted)
                .menu(menu)
                .build();
    }
}
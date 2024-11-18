package com.example.delivery.menu.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MenuOptionRequestDto {

    @Schema(description = "메뉴 옵션 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID menuOptionId;

    @Schema(description = "메뉴 ID", example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID menuId;

    @NotBlank(message = "옵션 이름은 필수입니다.")
    @Schema(description = "옵션 이름", example = "치즈 추가")
    private String optionName;

    @Positive(message = "옵션 가격은 0보다 커야 합니다.")
    @Schema(description = "옵션 가격", example = "500")
    private int optionPrice;

    @Schema(description = "삭제 여부", example = "false")
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
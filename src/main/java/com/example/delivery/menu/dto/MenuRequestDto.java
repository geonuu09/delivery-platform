package com.example.delivery.menu.dto;

import com.example.delivery.menu.entity.Menu;
import com.example.delivery.store.entity.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MenuRequestDto {

    @Schema(description = "가게 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID storeId;

    @Schema(description = "메뉴 ID", example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID menuId;

    @NotBlank(message = "메뉴 이름은 필수입니다.")
    @Schema(description = "메뉴 이름", example = "김치볶음밥")
    private String menuName;

    @Positive(message = "메뉴 가격은 0보다 커야 합니다.")
    @Schema(description = "메뉴 가격", example = "10000")
    private int menuPrice;

    @Schema(description = "삭제 여부", example = "false")
    private boolean deleted;

    @Schema(description = "숨김 여부", example = "false")
    private boolean hidden;

    @Schema(description = "메뉴 설명", example = "매콤한 맛이 일품인 김치볶음밥입니다.")
    private String menuDescription;

    @Schema(description = "메뉴 이미지 URL", example = "http://example.com/images/menu1.png")
    private String menuImage;

    public Menu toEntity(Store store) {

        return Menu.builder()
                .menuName(this.menuName)
                .menuPrice(this.menuPrice)
                .menuImage(this.menuImage)
                .menuDescription(this.menuDescription)
                .store(store)
                .build();
    }

}

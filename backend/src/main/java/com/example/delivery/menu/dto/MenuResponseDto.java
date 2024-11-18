package com.example.delivery.menu.dto;

import com.example.delivery.menu.entity.AiDescription;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MenuResponseDto {

    @Schema(description = "메뉴 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID menuId;

    @Schema(description = "메뉴 이름", example = "김치볶음밥")
    private String menuName;

    @Schema(description = "메뉴 가격", example = "10000")
    private int menuPrice;

    @Schema(description = "메뉴 설명", example = "매콤하고 맛있는 김치볶음밥")
    private String menuDescription;

    @Schema(description = "메뉴 이미지 URL", example = "http://example.com/images/menu1.png")
    private String menuImage;

    @Schema(description = "메뉴 옵션 리스트")
    private List<MenuOptionDto> menuOptions;

    @Schema(description = "AI 설명 리스트 (관리자/가게 주인 전용)")
    private List<AiDto> aiDescriptions;

    public MenuResponseDto(Menu menu) {
        this.menuId = menu.getMenuId();
        this.menuName = menu.getMenuName();
        this.menuPrice = menu.getMenuPrice();
        this.menuDescription = menu.getMenuDescription();
        this.menuImage = menu.getMenuImage();
    }

    public MenuResponseDto(Menu menu, boolean isOwnerOrAdmin) {
        this.menuId = menu.getMenuId();
        this.menuName = menu.getMenuName();
        this.menuPrice = menu.getMenuPrice();
        this.menuDescription = menu.getMenuDescription();
        this.menuImage = menu.getMenuImage();
        this.menuOptions = menu.getMenuOptions().stream()
                .map(MenuOptionDto::new)
                .collect(Collectors.toList());
        if(isOwnerOrAdmin) {
            this.aiDescriptions = menu.getAiDescriptions().stream()
                    .map(AiDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @NoArgsConstructor
    public static class MenuOptionDto {
        private String optionName;
        private int optionPrice;

        public MenuOptionDto(MenuOption option) {
            this.optionName = option.getOptionName();
            this.optionPrice = option.getOptionPrice();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class AiDto {
        private String aiQuestion;
        private String aiAnswer;

        public AiDto(AiDescription aiDescription) {
            this.aiQuestion = aiDescription.getAiQuestion();
            this.aiAnswer = aiDescription.getAiAnswer();
        }
    }
}
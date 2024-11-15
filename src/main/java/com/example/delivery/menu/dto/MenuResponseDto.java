package com.example.delivery.menu.dto;

import com.example.delivery.menu.entity.AiDescription;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.menu.entity.MenuOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MenuResponseDto {

    private UUID menuId;
    private String menuName;
    private int menuPrice;
    private String menuDescription;
    private String menuImage;
    private List<MenuOptionDto> menuOptions;
    private List<AiDto> aiDescriptions;

    public MenuResponseDto(Menu menu) {
        this.menuId = menu.getMenuId();
        this.menuName = menu.getMenuName();
        this.menuPrice = menu.getMenuPrice();
        this.menuDescription = menu.getMenuDescription();
        this.menuImage = menu.getMenuImage();
        this.menuOptions = menu.getMenuOptions().stream()
                .map(MenuOptionDto::new)
                .collect(Collectors.toList());
        this.aiDescriptions = menu.getAiDescriptions().stream()
                .map(AiDto::new)
                .collect(Collectors.toList());
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
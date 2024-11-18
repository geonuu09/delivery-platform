package com.example.delivery.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiDescriptionRequestDto {

    @Size(min = 1, max = 50, message = "질문은 1자 이상 50자 이하로 입력해야 합니다.")
    @Schema(description = "AI에게 전달할 질문", example = "메뉴 이름을 추천해줘")
    private String aiQuestion;

}

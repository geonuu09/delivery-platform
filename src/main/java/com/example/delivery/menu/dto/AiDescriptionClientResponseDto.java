package com.example.delivery.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AiDescriptionClientResponseDto {

    @Schema(description = "AI에게 전달된 질문", example = "메뉴 이름을 추천해줘")
    private String aiQuestion;

    @Schema(description = "AI가 반환한 답변", example = "추천 메뉴는 폭탄 김치볶음밥 입니다.")
    private String aiAnswer;

    public AiDescriptionClientResponseDto(AiDescriptionRequestDto aiDescriptionRequestDto, String aiAnswer) {
        this.aiQuestion = aiDescriptionRequestDto.getAiQuestion();
        this.aiAnswer = aiAnswer;
    }
}

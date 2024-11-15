package com.example.delivery.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AiDescriptionClientResponseDto {
    private String aiQuestion;
    private String aiAnswer;

    public AiDescriptionClientResponseDto(AiDescriptionRequestDto aiDescriptionRequestDto, String aiAnswer) {
        this.aiQuestion = aiDescriptionRequestDto.getAiQuestion();
        this.aiAnswer = aiAnswer;
    }
}

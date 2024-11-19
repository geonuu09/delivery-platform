package com.example.delivery.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AiDescriptionResponseDto {
    private List<Candidate> candidates;

    @Getter
    public static class Candidate {
        private Content content;
    }

    @Getter
    public static class Content {
        private List<Part> parts;
    }

    @Getter
    public static class Part {
        @Schema(description = "텍스트 내용", example = "추천 메뉴는 폭탄 김치볶음밥입니다.")
        private String text;
    }

}

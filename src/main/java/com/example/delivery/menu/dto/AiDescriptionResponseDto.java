package com.example.delivery.menu.dto;

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
        private String text;
    }

}

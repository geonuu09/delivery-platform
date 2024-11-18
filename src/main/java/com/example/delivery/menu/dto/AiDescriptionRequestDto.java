package com.example.delivery.menu.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiDescriptionRequestDto {

    @Size(min = 1, max = 50, message = "질문은 1자 이상 50자 이하로 입력해야 합니다.")
    private String aiQuestion;

}

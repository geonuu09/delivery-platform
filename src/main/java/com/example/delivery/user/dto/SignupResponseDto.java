package com.example.delivery.user.dto;

import com.example.delivery.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class SignupResponseDto {
    private Long userId;
    private String email;
    private String role;


    public SignupResponseDto(Long userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }
}

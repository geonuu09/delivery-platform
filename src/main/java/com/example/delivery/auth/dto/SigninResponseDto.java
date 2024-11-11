package com.example.delivery.auth.dto;

import lombok.Getter;

@Getter
public class SigninResponseDto {

    private String token;
    private String email;

    public SigninResponseDto(String token, String email) {
        this.token = token;
        this.email = email;

    }
}
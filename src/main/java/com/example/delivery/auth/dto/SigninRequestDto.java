package com.example.delivery.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SigninRequestDto {

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 2)
    private String password;
}
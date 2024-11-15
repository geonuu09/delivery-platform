package com.example.delivery.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SigninRequestDto {

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^[A-Za-z\\d@$!%*?&]{8,15}$")
    private String password;
}
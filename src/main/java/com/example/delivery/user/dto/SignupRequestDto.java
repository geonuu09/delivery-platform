package com.example.delivery.user.dto;

import com.example.delivery.user.entity.User.UserStatus;
import com.example.delivery.user.entity.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank()
    private String userName;
    @NotBlank()
    @Email()
    private String email;

    @NotBlank()
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$")
    private String password;

    @NotBlank()
    private String streetAddress;

    @NotBlank()
    private String detailAddress;

    @NotBlank()
    private String phoneNum;

    private UserRoleEnum role;

    private UserStatus status = UserStatus.ACTIVE;


}
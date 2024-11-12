package com.example.delivery.user.dto;

import com.example.delivery.user.entity.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class UserUpdateRequestDto {

    @NotBlank
    private String userName;

    @Size(min = 8, max = 15, message = "비밀번호는 8-15자리여야 합니다")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$")
    private String password;

    @NotBlank
    private String streetAddress;

    @NotBlank
    private String detailAddress;

    @NotBlank
    private String phoneNum;

    private UserRoleEnum role;

}


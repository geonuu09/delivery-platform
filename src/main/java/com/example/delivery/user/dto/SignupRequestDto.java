package com.example.delivery.user.dto;

import com.example.delivery.user.entity.User.UserStatus;
import com.example.delivery.user.entity.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[a-z0-9]{4,10}$",
        message = "아이디는 4~10자의 영문 소문자, 숫자만 사용 가능합니다.")
    private String userName;

    @NotBlank()
    @Email()
    private String email;

    @NotBlank()
    @Pattern(regexp = "^[A-Za-z\\d@$!%*?&]{8,15}$",
        message = "비밀번호는 8~15자의 영문 대/소문자, 숫자, 특수문자를 사용할 수 있습니다.")
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
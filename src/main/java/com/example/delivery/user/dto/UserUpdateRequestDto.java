package com.example.delivery.user.dto;

import com.example.delivery.user.entity.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUpdateRequestDto {
    @NotBlank(message = "사용자 이름은 필수입니다")
    private String userName;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 15, message = "비밀번호는 8-15자리여야 합니다")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$")
    private String password;

    @NotBlank(message = "도로명 주소는 필수입니다")
    private String streetAddress;

    @NotBlank(message = "상세 주소는 필수입니다")
    private String detailAddress;

    @NotBlank(message = "전화번호는 필수입니다")
    private String phoneNum;

    private UserRoleEnum role;

    // 관리자용 필드
    private Long userId;  // 관리자가 수정할 사용자의 ID

    @Builder
    public UserUpdateRequestDto(String userName, String email, String password,
        String streetAddress, String detailAddress,
        String phoneNum, UserRoleEnum role, Long userId) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
        this.phoneNum = phoneNum;
        this.role = role;
        this.userId = userId;
    }
}
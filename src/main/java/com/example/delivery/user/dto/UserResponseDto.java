package com.example.delivery.user.dto;

import com.example.delivery.user.entity.User;
import com.example.delivery.user.entity.UserRoleEnum;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private Long userId;
    private String userName;
    private String email;
    private String streetAddress;
    private String detailAddress;
    private String phoneNum;
    private UserRoleEnum role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.streetAddress = user.getStreetAddress();
        this.detailAddress = user.getDetailAddress();
        this.phoneNum = user.getPhoneNum();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getUpdatedAt();
        this.deletedAt = user.getDeletedAt();
    }
}
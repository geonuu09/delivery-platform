package com.example.delivery.user.entity;

import com.example.delivery.common.entity.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "p_users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate

public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false)
    private String phoneNum;

    public void updateUserInfo(
        String userName,
        String password,
        String phoneNum,
        String streetAddress,
        String detailAddress
    ) {
        if (userName != null) this.userName = userName;
        if (password != null) this.password = password;
        if (phoneNum != null) this.phoneNum = phoneNum;
        if (streetAddress != null) this.streetAddress = streetAddress;
        if (detailAddress != null) this.detailAddress = detailAddress;
    }

}


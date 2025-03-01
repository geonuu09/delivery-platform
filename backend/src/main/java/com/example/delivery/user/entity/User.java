package com.example.delivery.user.entity;

import com.example.delivery.bookmark.entity.Bookmark;
import com.example.delivery.cart.entity.Cart;
import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.order.entity.Order;
import com.example.delivery.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "p_user")
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

    private String profileImagePath;


    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Store> stores = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();


    public enum UserStatus {
        ACTIVE,
        INACTIVE,
    }

    public void updateUserInfo(
        String userName,
        String password,
        String phoneNum,
        String streetAddress,
        String detailAddress,
        String profileImagePath
    ) {
        if (userName != null) {
            this.userName = userName;
        }
        if (password != null) {
            this.password = password;
        }
        if (phoneNum != null) {
            this.phoneNum = phoneNum;
        }
        if (streetAddress != null) {
            this.streetAddress = streetAddress;
        }
        if (detailAddress != null) {
            this.detailAddress = detailAddress;
        }
        if (profileImagePath != null) {
            this.profileImagePath = profileImagePath;
        }
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    // 테스트코드 사용
    public void setRole(UserRoleEnum role) {
        this.role = role;
    }
}


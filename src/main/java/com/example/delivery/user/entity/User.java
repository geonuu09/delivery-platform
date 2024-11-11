package com.example.delivery.user.entity;

import com.example.delivery.bookmark.entity.Bookmark;
import com.example.delivery.cart.entity.Cart;
import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.order.entity.Order;
import com.example.delivery.user.dto.UserUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "p_users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder
    public User(String userName, String email, String password,
                String streetAddress, String detailAddress,
                String phoneNum, UserRoleEnum role) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
        this.phoneNum = phoneNum;
        this.role = role;
    }

    // 일반 사용자용 정보 업데이트 메소드
    public void updateBasicInfo(String password, String phoneNum, String streetAddress,
                                String detailAddress) {
        this.password = password;
        this.phoneNum = phoneNum;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
    }

    // 관리자용 전체 정보 업데이트 메소드
    public void updateAdminFields(UserUpdateRequestDto requestDto) {
        this.userName = requestDto.getUserName();
        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
        this.streetAddress = requestDto.getStreetAddress();
        this.detailAddress = requestDto.getDetailAddress();
        this.phoneNum = requestDto.getPhoneNum();

        if (requestDto.getRole() != null) {
            this.role = requestDto.getRole();
        }
    }
}

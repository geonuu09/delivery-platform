package com.example.delivery.store.entity;

import com.example.delivery.bookmark.entity.Bookmark;
import com.example.delivery.category.entity.Category;
import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.order.entity.Order;
import com.example.delivery.review.entity.Review;
import com.example.delivery.store.dto.StoreRequestDto;
import com.example.delivery.user.entity.User;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_store")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store extends Timestamped {

    @Id
    @UuidGenerator
    private UUID storeId; // 가게 ID

    @Column(length = 50, nullable = false, unique = true)
    private String storeName; // 상호명

    @Column(length = 50, nullable = false)
    private String storeOwnerName; // 사업자명

    @Column(nullable = false)
    private String storeLocation; // 가게 위치

    private boolean opened; // 가게 상태(영업중,마감)

    private boolean deleted; // 가게 삭제 여부

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<Review>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<Menu>();

    @OneToMany(mappedBy = "store", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Bookmark> bookmarks = new ArrayList<Bookmark>();

    @OneToMany(mappedBy = "store", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Order> orders = new ArrayList<Order>();

    public void update(StoreRequestDto storeRequestDto, Category category) {
        this.storeName = storeRequestDto.getStoreName() != null ? storeRequestDto.getStoreName() : this.storeName;
        this.storeOwnerName = storeRequestDto.getStoreOwnerName() != null ? storeRequestDto.getStoreOwnerName() : this.storeOwnerName;
        this.storeLocation = storeRequestDto.getStoreLocation() !=null ? storeRequestDto.getStoreLocation() : this.storeLocation;
        this.opened = storeRequestDto.isOpened();
        this.category = category != null ? category : this.category;
    }

    public void delete(String username) {
        this.deleted = true;
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(username);
    }

}

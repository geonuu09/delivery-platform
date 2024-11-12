package com.example.delivery.store.entity;

import com.example.delivery.bookmark.entity.Bookmark;
import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.menu.entity.Menu;
import com.example.delivery.order.entity.Order;
import com.example.delivery.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_stores")
@Getter
@Setter
@NoArgsConstructor
public class Store extends Timestamped {

    @Id
    private UUID storeId; // 가게 ID

    @Column(length = 50, nullable = false)
    private String storeName; // 상호명

    @Column(length = 50, nullable = false)
    private String storeOwnerName; // 사업자명

    @Column(length = 255, nullable = false)
    private String storeLocation; // 가게 위치

    @Column(nullable = false)
    private boolean isOpened; // 가게 상태(영업중,마감)

    @Column(nullable = false)
    private boolean isDeleted; // 가게 삭제 여부

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<Menu>();

    @OneToMany(mappedBy = "store", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Bookmark> bookmarks = new ArrayList<Bookmark>();

    @OneToMany(mappedBy = "store", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Order> orders = new ArrayList<Order>();

}

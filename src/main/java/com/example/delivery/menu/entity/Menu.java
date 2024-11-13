package com.example.delivery.menu.entity;

import com.example.delivery.cart.entity.Cart;
import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.menu.dto.MenuRequestDto;
import com.example.delivery.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "p_menus")
public class Menu extends Timestamped {

    @Id
    private UUID menuId;

    @Column(length = 50)
    private String menuName;

    @Column(length = 50)
    private int menuPrice;

    @Column
    private boolean deleted;

    @Column
    private boolean hidden;

    @Column(length = 255)
    private String menuDescription;

    @Column(length = 255)
    private String menuImage;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cart> carts = new ArrayList<Cart>();

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MenuOption> menuOptions = new ArrayList<>();

    public void update(MenuRequestDto menuRequestDto) {
        this.menuName = menuRequestDto.getMenuName();
        this.menuDescription = menuRequestDto.getMenuDescription();
        this.menuImage = menuRequestDto.getMenuImage();
        this.menuPrice = menuRequestDto.getMenuPrice();
    }

    public void delete(String username) {
        this.deleted = true;
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(username);
    }

}

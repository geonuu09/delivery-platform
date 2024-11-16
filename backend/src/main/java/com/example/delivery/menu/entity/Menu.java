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
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "p_menu")
public class Menu extends Timestamped {

    @Id
    @UuidGenerator
    private UUID menuId;

    @Column(length = 50, unique = true)
    private String menuName;

    @Column(nullable = false)
    private int menuPrice;

    private boolean deleted;

    private boolean hidden;

    private String menuDescription;

    private String menuImage;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cart> carts = new ArrayList<Cart>();

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MenuOption> menuOptions = new ArrayList<>();

    public void update(MenuRequestDto menuRequestDto) {
        this.menuName = menuRequestDto.getMenuName() != null ? menuRequestDto.getMenuName() : this.menuName;
        this.menuDescription = menuRequestDto.getMenuDescription() != null ? menuRequestDto.getMenuDescription() : this.menuDescription;
        this.menuImage = menuRequestDto.getMenuImage() != null ? menuRequestDto.getMenuImage() : this.menuImage;
        this.menuPrice = menuRequestDto.getMenuPrice() != 0 ? menuRequestDto.getMenuPrice() : this.getMenuPrice();
        this.hidden = menuRequestDto.isHidden();
    }

    public void delete(String username) {
        this.deleted = true;
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(username);
    }

}

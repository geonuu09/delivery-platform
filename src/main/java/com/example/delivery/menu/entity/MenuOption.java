package com.example.delivery.menu.entity;

import com.example.delivery.cart.entity.Cart;
import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.menu.dto.MenuOptionRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_menuOptions")
public class MenuOption extends Timestamped {

    @Id
    private UUID menuOptionId;

    @Column(length = 100)
    private String optionName;

    @Column
    private int optionPrice;

    @Column
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @OneToMany(mappedBy = "menuOption", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cart> carts = new ArrayList<Cart>();

    public void update(MenuOptionRequestDto menuOptionRequestDto) {
        this.optionName = menuOptionRequestDto.getOptionName();
        this.optionPrice = menuOptionRequestDto.getOptionPrice();
        this.deleted = menuOptionRequestDto.isDeleted();
    }

    public void delete(String username) {
        this.deleted = true;
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(username);
    }

}

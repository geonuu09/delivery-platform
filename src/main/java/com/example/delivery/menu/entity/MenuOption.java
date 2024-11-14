package com.example.delivery.menu.entity;

import com.example.delivery.cart.entity.Cart;
import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.menu.dto.MenuOptionRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

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
@Table(name = "p_menuOption")
public class MenuOption extends Timestamped {

    @Id
    @UuidGenerator
    private UUID menuOptionId;

    @Column(length = 100, unique = true, nullable = false)
    private String optionName;

    @Column(length = 100, unique = true, nullable = false)
    private int optionPrice;

    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToMany(mappedBy = "menuOptions", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cart> carts = new ArrayList<>();

    public void update(MenuOptionRequestDto menuOptionRequestDto) {
        this.optionName = menuOptionRequestDto.getOptionName() != null ? menuOptionRequestDto.getOptionName() : this.optionName;
        this.optionPrice = menuOptionRequestDto.getOptionPrice() != 0 ? menuOptionRequestDto.getOptionPrice() : this.optionPrice;
    }

    public void delete(String username) {
        this.deleted = true;
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(username);
    }

}

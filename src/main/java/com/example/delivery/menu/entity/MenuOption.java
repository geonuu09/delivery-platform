package com.example.delivery.menu.entity;

import com.example.delivery.cart.entity.Cart;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_menuOptions")
public class MenuOption {

    @Id
    private UUID menuOptionId;

    @Column(length = 100)
    private String optionName;

    @Column
    private int optionPrice;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @OneToMany(mappedBy = "menuOption", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Cart> carts = new ArrayList<Cart>();



}

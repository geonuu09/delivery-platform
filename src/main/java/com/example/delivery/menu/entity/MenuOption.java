package com.example.delivery.menu.entity;

import jakarta.persistence.*;

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
    @JoinColumn(name = "p_menus_id", nullable = false)
    private Menu menu;

}

package com.example.delivery.menu.entity;

import com.example.delivery.store.entity.Store;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_menus")
public class Menu {

    @Id
    private UUID menuId;

    @Column(length = 50)
    private String menuName;

    @Column(length = 50)
    private String menuPrice;

    @Column
    private Boolean isDeleted;

    @Column
    private Boolean isHidden;

    @Column(length = 255)
    private String menuDescription;

    @Column(length = 255)
    private String menuImage;

    @ManyToOne
    @JoinColumn(name = "p_stores_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuOption> menuOptionsList = new ArrayList<>();

}

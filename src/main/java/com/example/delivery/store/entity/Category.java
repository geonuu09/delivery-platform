package com.example.delivery.store.entity;

import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "p_categorys")
public class Category extends Timestamped {

    @Id
    private UUID categoryId;

    @Column(length = 50)
    private String categoryName;

    @Column
    private boolean isDeleted;

    @OneToMany(mappedBy = "category")
    private List<Store> stores = new ArrayList<Store>();

}

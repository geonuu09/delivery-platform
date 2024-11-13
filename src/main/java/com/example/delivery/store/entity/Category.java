package com.example.delivery.store.entity;

import com.example.delivery.common.entity.Timestamped;
import com.example.delivery.store.dto.CategoryRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "p_categorys")
public class Category extends Timestamped {

    @Id
    private UUID categoryId;

    @Column(nullable = false, unique = true)
    private String categoryName;

    @Column
    private boolean deleted;

    @OneToMany(mappedBy = "category")
    private List<Store> stores = new ArrayList<>();


    public void update(CategoryRequestDto categoryRequestDto) {
        this.categoryName = categoryRequestDto.getCategoryName();
        this.deleted = categoryRequestDto.isDeleted();
    }

    public void delete(String username) {
        this.deleted = true;
        this.setDeletedAt(LocalDateTime.now());
        this.setDeletedBy(username);
    }

}

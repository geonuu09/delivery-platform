package com.example.delivery.menu.repository;

import com.example.delivery.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID>, MenuRepositoryCustom {
    Optional<Menu> findByMenuIdAndStore_StoreIdAndDeletedFalse(UUID menuId, UUID storeId);

    Page<Menu> findByStore_StoreIdAndDeletedFalseAndHiddenFalse(UUID storeId, Pageable pageable);

}

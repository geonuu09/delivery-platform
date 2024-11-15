package com.example.delivery.menu.repository;

import com.example.delivery.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MenuRepositoryCustom {

    Page<Menu> findMenusByStore_StoreIdAndKeyword(UUID storeId, String keyword, Pageable pageable);

}

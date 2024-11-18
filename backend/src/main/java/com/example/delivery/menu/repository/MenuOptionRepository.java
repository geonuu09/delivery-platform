package com.example.delivery.menu.repository;

import com.example.delivery.menu.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface MenuOptionRepository extends JpaRepository<MenuOption, UUID> {

    Optional<MenuOption> findByMenuOptionIdAndDeletedFalse(UUID menuOptionId);

}


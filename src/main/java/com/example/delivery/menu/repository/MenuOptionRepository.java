package com.example.delivery.menu.repository;

import com.example.delivery.menu.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MenuOptionRepository extends JpaRepository<MenuOption, UUID> {

}

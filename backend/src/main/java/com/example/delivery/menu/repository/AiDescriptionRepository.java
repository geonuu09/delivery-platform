package com.example.delivery.menu.repository;

import com.example.delivery.menu.entity.AiDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AiDescriptionRepository extends JpaRepository<AiDescription, UUID> {

}

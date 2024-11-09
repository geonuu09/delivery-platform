package com.example.delivery.user.repository;

import com.example.delivery.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@NotBlank() @Email() String email);

    Optional<User> findByEmail(String email);
}

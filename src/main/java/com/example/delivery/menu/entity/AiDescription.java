package com.example.delivery.menu.entity;

import com.example.delivery.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Table(name = "p_ai")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AiDescription extends Timestamped {

    @Id
    @UuidGenerator
    private UUID aiDescriptionId;

    @Column(nullable = false)
    private String aiQuestion;

    @Column(nullable = false)
    private String aiAnswer;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

}

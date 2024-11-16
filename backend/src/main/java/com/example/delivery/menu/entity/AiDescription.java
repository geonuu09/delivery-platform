//package com.example.delivery.menu.entity;
//
//import com.example.delivery.common.entity.Timestamped;
//import com.example.delivery.user.entity.User;
//import jakarta.persistence.*;
//
//import java.util.UUID;
//
//@Table(name = "p_ai")
//@Entity
//public class AiDescription extends Timestamped {
//
//    @Id
//    private UUID aiDescriptionId;
//
//    @Column(nullable = false)
//    private String aiQuestion;
//
//    @Column(nullable = false)
//    private String aiAnswer;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//}

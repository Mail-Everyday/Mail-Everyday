package com.kme.maileverday.entity;


import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table (name = "userEmail", indexes = @Index(name = "idx_userEmail_email", columnList = "email"))
public class UserEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column(nullable = false)
    private LocalDateTime lastLoginDate;

    @Column
    private String accessToken;

    @Column
    private String refreshToken;

    @Column
    private String lastMailTime;
}

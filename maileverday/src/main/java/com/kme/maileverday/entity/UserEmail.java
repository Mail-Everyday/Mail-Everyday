package com.kme.maileverday.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table (indexes = @Index(name = "idx_userEmail_email", columnList = "email"))
public class UserEmail implements Serializable {
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

    @Builder
    public UserEmail(String email, String name, LocalDateTime registrationDate, LocalDateTime lastLoginDate,
                     String accessToken, String refreshToken, String lastMailTime) {
        this.email = email;
        this.name = name;
        this.registrationDate = registrationDate;
        this.lastLoginDate = lastLoginDate;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.lastMailTime = lastMailTime;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

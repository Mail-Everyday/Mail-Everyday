package com.kme.maileverday.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userEmail", referencedColumnName = "email")
    private UserEmail email;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean vacation;

    @Column
    private String vacationResponse;

    @Column(nullable = false)
    private LocalDateTime registrationDate;
}

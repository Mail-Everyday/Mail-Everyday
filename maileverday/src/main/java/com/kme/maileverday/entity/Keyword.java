package com.kme.maileverday.entity;

import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Builder
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userEmail", referencedColumnName = "email")
    private UserEmail userEmail;

    private String keyword;

    private boolean active;

    private boolean vacation;

    private String vacationResponse;

    private LocalDateTime registrationDate;


}

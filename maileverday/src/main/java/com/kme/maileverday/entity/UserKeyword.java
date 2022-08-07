package com.kme.maileverday.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table (indexes = {
        @Index(name="idx_userKeyword_email", columnList = "userEmail"),
        @Index(name="idx_userKeyword_keyword", columnList = "keyword")
})
public class UserKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userEmail", referencedColumnName = "email", nullable = false)
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

    @Builder
    public UserKeyword(UserEmail email, String keyword, boolean active, boolean vacation,
                       String vacationResponse, LocalDateTime registrationDate) {
        this.email = email;
        this.keyword = keyword;
        this.active = active;
        this.vacation = vacation;
        this.vacationResponse = vacationResponse;
        this.registrationDate = registrationDate;
    }
}

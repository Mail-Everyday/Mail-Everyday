package com.kme.maileverday.entity;

import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.CustomMessage;
import com.kme.maileverday.web.dto.keyword.KeywordUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
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
    private Byte filterType;

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

    public void update(KeywordUpdateRequestDto requestDto) throws CustomException {
        switch (requestDto.getUpdateRequestType()) {
            case MESSAGE_UPDATE:
                this.vacationResponse = requestDto.getVacationMessage();
                break;

            case ACTIVE_UPDATE:
                this.active = requestDto.isActive();
                break;

            case VACATION_UPDATE:
                this.vacation = requestDto.isVacation();
                break;

            case UNKNOWN_REQUEST:
            default:
                throw new CustomException(CustomMessage.BAD_REQUEST);
        }
    }
}

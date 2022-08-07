package com.kme.maileverday.web.dto.keyword;

import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserKeyword;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class KeywordSaveRequestDto {
    private String keyword;
    private String userEmail;
    private String vacationMessage;

    @Setter
    private boolean active;

    @Setter
    private boolean vacation;

    @Setter
    private LocalDateTime registrationDate;

    public UserKeyword toEntity(UserEmail userEmail) {
        return UserKeyword.builder()
                .email(userEmail)
                .keyword(keyword)
                .active(false)
                .vacation(false)
                .vacationResponse(vacationMessage)
                .registrationDate(LocalDateTime.now())
                .build();
    }
}
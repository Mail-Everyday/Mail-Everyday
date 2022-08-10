package com.kme.maileverday.web.dto.keyword;

import com.kme.maileverday.entity.UserKeyword;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KeywordResponseDto {
    private Long id;
    private String userEmail;
    private String keyword;
    private String vacationResponse;
    private boolean active;
    private boolean vacation;

    @Builder
    public KeywordResponseDto(UserKeyword entity) {
        this.id = entity.getId();
        this.userEmail = entity.getEmail().getEmail();
        this.keyword = entity.getKeyword();
        this.vacationResponse = entity.getVacationResponse();
        this.active = entity.isActive();
        this.vacation = entity.isVacation();
    }
}

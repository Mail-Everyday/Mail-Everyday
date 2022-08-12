package com.kme.maileverday.web.dto.keyword;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KeywordUpdateRequestDto {
    private UpdateRequestType updateRequestType;
    private String keyword;
    private String vacationMessage;
    private boolean active;
    private boolean vacation;

    @Builder
    KeywordUpdateRequestDto(String updateRequestType, String keyword, String vacationMessage, boolean active, boolean vacation) {
        this.updateRequestType = UpdateRequestType.from(updateRequestType);
        this.keyword = keyword;
        this.vacationMessage = vacationMessage;
        this.active = active;
        this.vacation = vacation;
    }
}


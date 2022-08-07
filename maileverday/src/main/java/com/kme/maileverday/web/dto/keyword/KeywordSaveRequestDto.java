package com.kme.maileverday.web.dto.keyword;

import lombok.Getter;

@Getter
public class KeywordSaveRequestDto {
    private String keyword;
    private String userEmail;
    private String vacationMessage;
}

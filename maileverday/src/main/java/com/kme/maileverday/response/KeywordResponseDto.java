package com.kme.maileverday.response;

import com.kme.maileverday.entity.UserEmail;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class KeywordResponseDto {
    private Long id;
    private boolean active;
    private String keyword;
    private LocalDateTime registrationDate;
    private boolean vacation;
    private String vacationResponse;
    private UserEmail email;
    private Byte filterType;
}

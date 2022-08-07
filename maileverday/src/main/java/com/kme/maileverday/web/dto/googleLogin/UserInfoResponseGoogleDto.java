package com.kme.maileverday.web.dto.googleLogin;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserInfoResponseGoogleDto {
    private String email;
    private String name;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLoginDate;
    private String lastMailTime;

    @Builder
    public UserInfoResponseGoogleDto(String email, String name, LocalDateTime registrationDate, LocalDateTime lastLoginDate,
                                     String lastMailTime) {
        this.email = email;
        this.name = name;
        this.registrationDate = registrationDate;
        this.lastLoginDate = lastLoginDate;
        this.lastMailTime = lastMailTime;
    }
}

package com.kme.maileverday.web.dto.googleLogin;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginServiceResponseDto {
    private String userEmail;
    private String userName;

    @Builder
    LoginServiceResponseDto(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
    }
}

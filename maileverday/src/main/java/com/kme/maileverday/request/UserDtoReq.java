package com.kme.maileverday.request;

import com.kme.maileverday.entity.UserEmail;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserDtoReq {

    private String email;
    private String name;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLoginDate;
    private String accessToken;
    private String refreshToken;
    private String lastMailTime;



}

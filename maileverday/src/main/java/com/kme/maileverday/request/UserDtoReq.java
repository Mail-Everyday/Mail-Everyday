package com.kme.maileverday.request;

import com.kme.maileverday.entity.UserEmail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UserDtoReq {

    private String email;
    private String name;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLoginDate;
    private String accessToken;
    private String refreshToken;
    private String lastMailTime;


    public UserEmail toEntity(){
        UserEmail build = UserEmail.builder()
                .email(email)
                .name(name)
                .registrationDate(registrationDate)
                .lastLoginDate(lastLoginDate)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .lastMailTime(lastMailTime)
                .build();

        return build;
    }

}

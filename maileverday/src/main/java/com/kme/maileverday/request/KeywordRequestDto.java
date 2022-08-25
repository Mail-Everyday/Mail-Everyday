package com.kme.maileverday.request;

import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserKeyword;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class KeywordRequestDto {

    private UserEmail email;

    private String keyword;

    private Byte filterType;

    private boolean active;

    private boolean vacation;

    private String vacationResponse;

    private LocalDateTime registrationDate;



    public UserKeyword toEntity(){
        UserKeyword build = UserKeyword.builder()
                .email(email)
                .keyword(keyword)
                .filterType(filterType)
                .active(active)
                .vacation(vacation)
                .vacationResponse(vacationResponse)
                .registrationDate(registrationDate)
                .build();

        return build;
    }
}

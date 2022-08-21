package com.kme.maileverday.web.dto.googleLogin;

import com.kme.maileverday.entity.UserEmail;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PhoneUpdateResponseDto {
    private String userEmail;
    private String phoneNo;

    @Builder
    public PhoneUpdateResponseDto(UserEmail entity) {
        this.userEmail = entity.getEmail();
        if (entity.getPhone() == null)
            this.phoneNo = "";
        else
            this.phoneNo = entity.getPhone();
    }
}

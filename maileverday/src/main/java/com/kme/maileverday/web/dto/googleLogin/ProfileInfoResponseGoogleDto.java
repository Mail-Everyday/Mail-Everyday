package com.kme.maileverday.web.dto.googleLogin;

import lombok.Getter;

import java.util.List;

@Getter
public class ProfileInfoResponseGoogleDto {
    private List<ProfileInfoNamesGoogleDto> names;
}
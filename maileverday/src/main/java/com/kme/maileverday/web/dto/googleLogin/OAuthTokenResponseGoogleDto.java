package com.kme.maileverday.web.dto.googleLogin;

import lombok.Getter;

@Getter
public class OAuthTokenResponseGoogleDto {
    private String access_token;
    private String refresh_token;
    private long expires_in;
}

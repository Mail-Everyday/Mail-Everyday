package com.kme.maileverday.web.dto.googleLogin;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class OAuthTokenRequestGoogleDto {
    String grantType;
    String clientId;
    String clientSecret;
    String code;
    String redirectUri;
    String refreshToken;

    @Builder
    OAuthTokenRequestGoogleDto(String grantType, String clientId, String clientSecret,
                               String code, String redirectUri, String refreshToken) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
        this.redirectUri = redirectUri;
        this.refreshToken = refreshToken;
    }

    public MultiValueMap<String, String> toEntity() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", grantType);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("code", code);
        map.add("redirect_uri", redirectUri);
        map.add("refresh_token", refreshToken);
        return map;
    }
}

package com.kme.maileverday.entity;

import com.kme.maileverday.utility.EnvironmentKey;
import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.ErrorMessage;
import com.kme.maileverday.web.dto.OAuthTokenRequestGoogleDto;
import com.kme.maileverday.web.dto.OAuthTokenResponseGoogleDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Getter
public class Token {
    private String access_token;
    private String refresh_token;
    private long expires_in;

    @Builder
    public Token(String access_token, String refresh_token, long expires_in) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
    }

    public static Token getToken(String authCode) throws CustomException {
        final String url = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> map = OAuthTokenRequestGoogleDto.builder()
                .clientId(EnvironmentKey.getGoogleApiKey())
                .clientSecret(EnvironmentKey.getGoogleApiSecret())
                .redirectUri(EnvironmentKey.getGoogleApiRedirectUri())
                .grantType("authorization_code")
                .code(authCode)
                .build().toEntity();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<OAuthTokenResponseGoogleDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, OAuthTokenResponseGoogleDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Token.builder()
                    .access_token(response.getBody().getAccess_token())
                    .refresh_token(response.getBody().getRefresh_token())
                    .expires_in(response.getBody().getExpires_in())
                    .build();
        }
        else {
            throw new CustomException(ErrorMessage.NEED_AUTH_PERMISSION);
        }
    }

    private Token getTokenByRefreshToken() throws CustomException {
        final String url = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> map = OAuthTokenRequestGoogleDto.builder()
                .clientId(EnvironmentKey.getGoogleApiKey())
                .clientSecret(EnvironmentKey.getGoogleApiSecret())
                .redirectUri(EnvironmentKey.getGoogleApiRedirectUri())
                .grantType("refresh_token")
                .refreshToken(refresh_token)
                .build().toEntity();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<OAuthTokenResponseGoogleDto> response = restTemplate.exchange(url, HttpMethod.POST, entity, OAuthTokenResponseGoogleDto.class);
            return Token.builder()
                    .access_token(response.getBody().getAccess_token())
                    .expires_in(response.getBody().getExpires_in())
                    .build();
        } catch (HttpClientErrorException e) {
            // 리프레쉬 토큰이 유효하지 않음
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new CustomException(ErrorMessage.REFRESH_TOKEN_INVALID);
            }
            throw e;
        }
    }

    // 토큰이 만료되었으면 refresh_token을 이용하여 갱신
    public void updateIfExpired() throws CustomException {
        final String url = "https://www.googleapis.com/oauth2/v2/tokeninfo";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity entity = new HttpEntity(headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return ;
        } catch (HttpClientErrorException e) {
            // 토큰 만료시 Google 서버에서 [400, Bad Request]
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                Token newToken = getTokenByRefreshToken();
                access_token = newToken.getAccess_token();
                expires_in = newToken.getExpires_in();
                newToken = null;
            }
        }
    }

    public <T> T callApi(final String url, Class<T> responseDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity entity = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET,
                entity, responseDto);
        return response.getBody();
    }
}
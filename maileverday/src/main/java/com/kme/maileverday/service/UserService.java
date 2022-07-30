package com.kme.maileverday.service;

import com.kme.maileverday.entity.UserEmailRepository;
import com.kme.maileverday.utility.EnvironmentKey;
import com.kme.maileverday.web.dto.OAuthTokenRequestGoogleDto;
import com.kme.maileverday.web.dto.OAuthTokenResponseGoogleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserEmailRepository userEmailRepository;

    @Transactional
    public String googleLogin(String authCode) throws Exception {
        OAuthTokenResponseGoogleDto oAuthTokenResponseGoogleDto = getToken(authCode);

    }

    private OAuthTokenResponseGoogleDto getToken(String authCode) throws Exception{
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
            return response.getBody();
        }
        else {
            throw new Exception();
        }
    }
}

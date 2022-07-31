package com.kme.maileverday.service;

import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserEmailRepository;
import com.kme.maileverday.utility.EnvironmentKey;
import com.kme.maileverday.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class GoogleUserService {
    private final UserEmailRepository userEmailRepository;

    @Transactional
    public String login(String authCode) throws Exception {
        OAuthTokenResponseGoogleDto token = getToken(authCode);
        UserInfoResponseGoogleDto userInfo = getUserInfo(token.getAccess_token());
        System.out.println(token.getAccess_token()); // Debug

        UserEmail user = userEmailRepository.findByEmail(userInfo.getEmail());
        if (user != null) {
            user.setAccessToken(token.getAccess_token());
            user.setRefreshToken(token.getRefresh_token());
            user.setLastLoginDate(userInfo.getLastLoginDate());
            user.setLastMailTime(userInfo.getLastMailTime());
            userEmailRepository.save(user);
        }
        else {
            user = UserEmail.builder()
                    .email(userInfo.getEmail())
                    .name(userInfo.getName())
                    .registrationDate(userInfo.getRegistrationDate())
                    .lastLoginDate(userInfo.getLastLoginDate())
                    .accessToken(token.getAccess_token())
                    .refreshToken(token.getRefresh_token())
                    .lastMailTime(userInfo.getLastMailTime())
                    .build();
            userEmailRepository.save(user);
        }
        return user.getName();
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

    private UserInfoResponseGoogleDto getUserInfo(String accessToken) {
        ProfileInfoResponseGoogleDto profileInfo = getProfileInfo(accessToken);
        MailInfoResponseGoogleDto mailInfo = getMailInfo(accessToken);

        return UserInfoResponseGoogleDto.builder()
                .email(mailInfo.getEmailAddress())
                .name(profileInfo.getNames().get(0).getDisplayName())
                .registrationDate(LocalDateTime.now())
                .lastLoginDate(LocalDateTime.now())
                .lastMailTime(LocalDateTime.now().toString())
                .build();
    }

    private ProfileInfoResponseGoogleDto getProfileInfo(String accessToken) {
        final String url = "https://people.googleapis.com/v1/people/me?personFields=names";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ProfileInfoResponseGoogleDto> response = restTemplate.exchange(url, HttpMethod.GET,
                entity, ProfileInfoResponseGoogleDto.class);
        return response.getBody();
    }

    private MailInfoResponseGoogleDto getMailInfo(String accessToken) {
        final String url = "https://gmail.googleapis.com/gmail/v1/users/me/profile";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MailInfoResponseGoogleDto> response = restTemplate.exchange(url, HttpMethod.GET,
                entity, MailInfoResponseGoogleDto.class);
        return response.getBody();
    }
}
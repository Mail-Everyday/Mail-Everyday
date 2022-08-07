package com.kme.maileverday.service;

import com.kme.maileverday.entity.Token;
import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserEmailRepository;
import com.kme.maileverday.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GoogleUserService {
    private final UserEmailRepository userEmailRepository;

    @Value("${GOOGLE-API-DATETIME-KEY-NAME}")
    private String dateKeyName;

    @Transactional
    public LoginServiceResponseDto login(String authCode) throws Exception {
        Token token = Token.getToken(authCode);
        UserInfoResponseGoogleDto userInfo = getUserInfo(token);

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
        return LoginServiceResponseDto.builder()
                .userEmail(userInfo.getEmail())
                .userName(userInfo.getName())
                .build();
    }

    private UserInfoResponseGoogleDto getUserInfo(Token token) {
        final String urlGetProfileInfo = "https://people.googleapis.com/v1/people/me?personFields=names";
        final String urlGetMailInfo = "https://gmail.googleapis.com/gmail/v1/users/me/profile";

        ProfileInfoResponseGoogleDto profileInfo = token.callApi(urlGetProfileInfo, ProfileInfoResponseGoogleDto.class);
        MailInfoResponseGoogleDto mailInfo = token.callApi(urlGetMailInfo, MailInfoResponseGoogleDto.class);
        String lastMailTime = getLastMailTime(token);

        return UserInfoResponseGoogleDto.builder()
                .email(mailInfo.getEmailAddress())
                .name(profileInfo.getNames().get(0).getDisplayName())
                .registrationDate(LocalDateTime.now())
                .lastLoginDate(LocalDateTime.now())
                .lastMailTime(lastMailTime)
                .build();
    }

    private String getLastMailTime(Token token) {
        MessageListResponseGoogleDto msgList = getMessageList(token, null);

        String lastMsgId = msgList.getMessages().get(0).getId();
        if (lastMsgId == null) {
            // 메일함에 메일이 하나도 없을때 예외처리
            return LocalDateTime.now().toString();
        }
        MessageResponseGoogleDto msg = getMessageResponse(token, lastMsgId);
        List<MessageHeaderGoogleDto> msgHeader = msg.getPayload().getHeaders();
        for (int i = 0; i < msgHeader.size(); i++) {
            if (msgHeader.get(i).getName().equals(dateKeyName)) {
                return msgHeader.get(i).getValue();
            }
        }
        return LocalDateTime.now().toString();
    }

    private MessageListResponseGoogleDto getMessageList(Token token, String pageToken) {
        String url = "https://gmail.googleapis.com/gmail/v1/users/me/messages";

        if (pageToken != null) {
            url += "?pageToken=" + pageToken;
        }
        return token.callApi(url, MessageListResponseGoogleDto.class);
    }

    private MessageResponseGoogleDto getMessageResponse(Token token, String msgId) {
        final String url = "https://gmail.googleapis.com/gmail/v1/users/me/messages/" + msgId;
        return token.callApi(url, MessageResponseGoogleDto.class);
    }
}
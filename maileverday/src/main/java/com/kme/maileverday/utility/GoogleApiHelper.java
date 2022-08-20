package com.kme.maileverday.utility;

import com.kme.maileverday.entity.Token;
import com.kme.maileverday.web.dto.googleLogin.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class GoogleApiHelper {

    public static MessageListResponseGoogleDto getMessageList(Token token, String pageToken) {
        String url = "https://gmail.googleapis.com/gmail/v1/users/me/messages";

        if (pageToken != null) {
            url += "?pageToken=" + pageToken;
        }
        return token.callApi(url, MessageListResponseGoogleDto.class);
    }

    public static MessageResponseGoogleDto getMessageResponse(Token token, String msgId) {
        final String url = "https://gmail.googleapis.com/gmail/v1/users/me/messages/" + msgId;
        return token.callApi(url, MessageResponseGoogleDto.class);
    }

    public static UserInfoResponseGoogleDto getUserInfo(Token token) {
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

    public static String getLastMailTime(Token token) {
        MessageListResponseGoogleDto msgList = getMessageList(token, null);
        if (msgList.getResultSizeEstimate() == 0) {
            // 메일함에 메일이 하나도 없을때 예외처리
            return DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now());
        }

        String lastMsgId = msgList.getMessages().get(0).getId();
        MessageResponseGoogleDto msg = getMessageResponse(token, lastMsgId);
        List<MessageHeaderGoogleDto> msgHeader = msg.getPayload().getHeaders();
        for (int i = 0; i < msgHeader.size(); i++) {
            if (msgHeader.get(i).getName().equals(EnvironmentKey.getDateKeyName())) {
                return msgHeader.get(i).getValue();
            }
        }
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now());
    }
}

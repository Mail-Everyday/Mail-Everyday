package com.kme.maileverday.entity;

import com.kme.maileverday.utility.EnvironmentKey;
import com.kme.maileverday.utility.GoogleApiHelper;
import com.kme.maileverday.utility.UtilityFunctions;
import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.web.dto.googleLogin.MessageHeaderGoogleDto;
import com.kme.maileverday.web.dto.googleLogin.MessageIdGoogleDto;
import com.kme.maileverday.web.dto.googleLogin.MessageListResponseGoogleDto;
import com.kme.maileverday.web.dto.googleLogin.MessageResponseGoogleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class EmailHandler {

    public List<Email> getNewEmails(UserEmail user) {
        Token token = Token.builder()
                .access_token(user.getAccessToken())
                .refresh_token(user.getRefreshToken())
                .build();

        try {
            if (token.updateIfExpired()) {
                user.updateAccessToken(token.getAccess_token());
            }
        } catch (CustomException e) {
            e.printStackTrace();
        }

        return findNewEmails(token, GoogleApiHelper.getMessageList(token, null),
                UtilityFunctions.exchangeToLocalDateTime(user.getLastMailTime()));
    }

    private List<Email> findNewEmails(Token token, MessageListResponseGoogleDto msgList, LocalDateTime lastDateTime) {
        int i = 0;
        List<Email> emails;
        List<MessageIdGoogleDto> msgIds= msgList.getMessages();

        while (i < msgIds.size()) {
            if (!isNewEmail(token, msgIds.get(i), lastDateTime)) {
                break;
            }
            else {

            }
            i++;
        }
        return emails;
    }

    private boolean isNewEmail(Token token, MessageIdGoogleDto msgId, LocalDateTime lastDateTime) {
        MessageResponseGoogleDto msg = GoogleApiHelper.getMessageResponse(token, msgId.getId());
        if (parseDate(msg).isAfter(lastDateTime))
            return true;
        else
            return false;
    }

    private LocalDateTime parseDate(MessageResponseGoogleDto msg) {
        List<MessageHeaderGoogleDto> msgHeaders = msg.getPayload().getHeaders();

        for (int i = 0; i < msgHeaders.size(); i++) {
            if (msgHeaders.get(i).getName().equals(EnvironmentKey.getDateKeyName())) {
                return UtilityFunctions.exchangeToLocalDateTime(msgHeaders.get(i).getValue());
            }
        }
        return null;
    }
}

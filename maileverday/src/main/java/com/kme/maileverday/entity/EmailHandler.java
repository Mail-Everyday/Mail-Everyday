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
import java.util.ArrayList;
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
        List<Email> emails = new ArrayList<Email>();
        List<MessageIdGoogleDto> msgIds = msgList.getMessages();
        String nextPage = msgList.getNextPageToken();

        while (i < msgIds.size()) {
            if (!isNewEmail(token, msgIds.get(i), lastDateTime)) {
                break;
            }
            else {
                MessageResponseGoogleDto msg = GoogleApiHelper.getMessageResponse(token, msgIds.get(i).getId());
                emails.add(parseEmail(msg));

                if (i == msgIds.size() - 1 && nextPage != null) {
                    // 다음 페이지도 확인
                    i = -1;
                    MessageListResponseGoogleDto temp = GoogleApiHelper.getMessageList(token, nextPage);
                    nextPage = temp.getNextPageToken();
                    msgIds = temp.getMessages();
                }
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

    private Email parseEmail(MessageResponseGoogleDto msg) {
        String msgId = msg.getId();
        String snippet = msg.getSnippet();

        String from = null;
        String subject = null;
        LocalDateTime dateTime = null;
        List<MessageHeaderGoogleDto> headers = msg.getPayload().getHeaders();
        for (int i = 0; i < headers.size(); i++) {
            switch (headers.get(i).getName()) {
                case "From":
                    from = headers.get(i).getValue();
                    break;

                case "Subject":
                    subject = headers.get(i).getValue();
                    break;

                case "Date":
                    dateTime = UtilityFunctions.exchangeToLocalDateTime(headers.get(i).getValue());
                    break;
            }
        }
        return Email.builder()
                .id(msgId)
                .from(from)
                .subject(subject)
                .snippet(snippet)
                .date(dateTime)
                .build();
    }
}

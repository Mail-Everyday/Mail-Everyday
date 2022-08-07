package com.kme.maileverday.web.dto.googleLogin;

import lombok.Getter;

import java.util.List;

@Getter
public class MessageListResponseGoogleDto {
    private List<MessageIdGoogleDto> messages;
    private String nextPageToken;
    private int resultSizeEstimate;
}

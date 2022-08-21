package com.kme.maileverday.web.dto.ncloudsens;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SendSMSRequestDto {
    private SMSTypeEnum type;
    private SMSContentType contentType;
    private String countryCode;
    private String from;
    private String subject;
    private String content;
    private List<MessagesObject> messages;
    private String reserveTime;
    private String reserveTimeZone;
    private String scheduleCode;

    @Builder
    public SendSMSRequestDto(SMSTypeEnum type, SMSContentType contentType, String countryCode, String from, String subject, String content, List<MessagesObject> messages) {
        this.type = type;
        this.contentType = contentType;
        this.countryCode = countryCode;
        this.from = from;
        this.subject = subject;
        this.content = content;
        this.messages = messages;
    }
}

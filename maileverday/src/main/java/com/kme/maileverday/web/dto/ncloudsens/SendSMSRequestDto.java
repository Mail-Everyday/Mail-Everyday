package com.kme.maileverday.web.dto.ncloudsens;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

    public MultiValueMap<String, String> toJSONMap() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("type", type.toString());
        map.add("contentType", contentType.toString());
        map.add("countryCode", countryCode);
        map.add("from", from);
        map.add("subject", subject);
        map.add("content", content);
        map.add("messages", messages.toString());
        map.add("reserveTime", reserveTime);
        map.add("reserveTimeZone", reserveTimeZone);
        map.add("scheduleCode", scheduleCode);
        return map;
    }
}

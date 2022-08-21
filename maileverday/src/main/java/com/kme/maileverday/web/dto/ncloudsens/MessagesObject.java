package com.kme.maileverday.web.dto.ncloudsens;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessagesObject {
    private String to;
    private String subject;
    private String content;

    @Builder
    public MessagesObject(String to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }
}

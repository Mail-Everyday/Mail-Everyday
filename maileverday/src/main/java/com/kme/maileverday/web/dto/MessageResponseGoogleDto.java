package com.kme.maileverday.web.dto;

import lombok.Getter;

@Getter
public class MessageResponseGoogleDto {
    private String id;
    private String threadId;
    private String snippet;
    private MessageBodyGoogleDto payload;
}

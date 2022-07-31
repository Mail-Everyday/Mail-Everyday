package com.kme.maileverday.web.dto;

import lombok.Getter;

@Getter
public class MailInfoResponseGoogleDto {
    private String emailAddress;
    private String messageTotal;
    private String threadsTotal;
    private String historyId;
}

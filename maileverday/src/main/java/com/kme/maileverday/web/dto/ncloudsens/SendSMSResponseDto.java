package com.kme.maileverday.web.dto.ncloudsens;

import lombok.Getter;

@Getter
public class SendSMSResponseDto {
    private String requestId;
    private String requestTime;
    private String statusCode;
    private String statusName;
}

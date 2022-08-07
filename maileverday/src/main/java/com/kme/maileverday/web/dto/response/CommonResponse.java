package com.kme.maileverday.web.dto.response;

import lombok.Getter;

@Getter
public class CommonResponse {
    protected boolean success;
    protected int code;
    protected String message;
}

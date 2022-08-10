package com.kme.maileverday.utility.exception;

import lombok.Getter;

@Getter
public class CustomException extends Exception {
    private String code;
    private String message;
    private int httpCode;

    public CustomException(CustomMessage message) {
        this.code = message.getCode();
        this.message = message.getDesc();
        this.httpCode = message.getHttpCode();
    }
}

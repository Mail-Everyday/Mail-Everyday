package com.kme.maileverday.utility.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    NEED_AUTH_PERMISSION ("NEED_AUTH_PERMISSION", "OAuth 권한 승인 필요"),
    TOKEN_INVALID ("TOKEN_INVALID", "유효하지 않은 토큰"),
    REFRESH_TOKEN_INVALID ("REFRESH_TOKEN_INVALID", "유효하지 않은 리프레쉬 토큰");

    private String code;
    private String desc;
}

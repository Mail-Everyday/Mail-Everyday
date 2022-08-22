package com.kme.maileverday.utility.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomMessage {
    OK("OK", "OK", 200),
    BAD_REQUEST("BAD_REQUEST", "잘못된 요청입니다.", 400),
    PHONE_NUMBER_FORMAT_INVALIDATED("PHONE_NUMBER_FORMAT_INVALIDATED", "입력된 핸드폰 번호가 올바르지 않습니다. (예시)01012345678", 400),
    NEED_AUTH_PERMISSION ("NEED_AUTH_PERMISSION", "OAuth 권한 승인 필요", 401),
    TOKEN_INVALID ("TOKEN_INVALID", "유효하지 않은 토큰", 401),
    REFRESH_TOKEN_INVALID ("REFRESH_TOKEN_INVALID", "유효하지 않은 리프레쉬 토큰", 401),
    USER_EMAIL_NOT_FOUND("USER_EMAIL_NOT_FOUND", "유저 이메일을 찾을 수 없습니다", 401),
    KEYWORD_NOT_FOUND("KEYWORD_NOT_FOUND", "해당 키워드를 찾을 수 없습니다.", 401),
    FORBIDDEN("FORBIDDEN", "권한이 없습니다.", 403);

    private String code;
    private String desc;
    private int httpCode;
}

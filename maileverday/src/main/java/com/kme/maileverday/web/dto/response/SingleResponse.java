package com.kme.maileverday.web.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SingleResponse<T> extends CommonResponse {
    private T data;

    @Builder
    SingleResponse(T data, int httpStatusCode, boolean success, String message) {
        this.data = data;
        this.code = httpStatusCode;
        this.success = success;
        this.message = message;
    }
}

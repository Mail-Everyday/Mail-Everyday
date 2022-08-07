package com.kme.maileverday.web.dto.response;

import com.kme.maileverday.utility.exception.ErrorMessage;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SingleResponse<T> extends CommonResponse {
    private T data;

    @Builder
    SingleResponse(T data, int httpStatusCode, boolean success, ErrorMessage message) {
        this.data = data;
        this.code = httpStatusCode;
        this.success = success;
        this.message = message.getDesc();
    }
}

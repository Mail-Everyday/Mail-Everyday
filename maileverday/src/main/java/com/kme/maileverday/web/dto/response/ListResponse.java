package com.kme.maileverday.web.dto.response;

import com.kme.maileverday.utility.exception.ErrorMessage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ListResponse<T> extends CommonResponse{
    private List<T> dataList;

    @Builder
    ListResponse(List<T> dataList, int httpStatusCode, boolean success, ErrorMessage message) {
        this.dataList = dataList;
        this.code = httpStatusCode;
        this.success = success;
        this.message = message.getDesc();
    }
}

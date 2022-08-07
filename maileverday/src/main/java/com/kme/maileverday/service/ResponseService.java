package com.kme.maileverday.service;

import com.kme.maileverday.utility.exception.ErrorMessage;
import com.kme.maileverday.web.dto.response.ListResponse;
import com.kme.maileverday.web.dto.response.SingleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {
    public <T> SingleResponse<T> getSingleResponse(T data, int httpStatusCode, boolean success, ErrorMessage message) {
        return SingleResponse.<T>builder()
                .data(data)
                .httpStatusCode(httpStatusCode)
                .success(success)
                .message(message)
                .build();
    }

    public <T> ListResponse<T> getListResponse(List<T> dataList, int httpStatusCode, boolean success, ErrorMessage message) {
        return ListResponse.<T>builder()
                .dataList(dataList)
                .httpStatusCode(httpStatusCode)
                .success(success)
                .message(message)
                .build();
    }
}
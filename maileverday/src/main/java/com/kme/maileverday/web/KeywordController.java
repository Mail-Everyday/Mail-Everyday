package com.kme.maileverday.web;

import com.kme.maileverday.entity.UserKeyword;
import com.kme.maileverday.service.KeywordService;
import com.kme.maileverday.service.ResponseService;
import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.ErrorMessage;
import com.kme.maileverday.web.dto.keyword.KeywordSaveRequestDto;
import com.kme.maileverday.web.dto.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@RequiredArgsConstructor
@Controller
public class KeywordController {
    private final KeywordService keywordService;
    private final ResponseService responseService;

    @PostMapping("/api/v1/keywords")
    @ResponseBody
    public SingleResponse<String> save(@RequestBody KeywordSaveRequestDto requestDto) {
        try {
            keywordService.save(requestDto);
            return responseService.getSingleResponse(null, 200, true, ErrorMessage.OK.getDesc());
        } catch (CustomException e) {
            if (e.getCode().equals(ErrorMessage.USER_EMAIL_NOT_FOUND.getCode())) {
                return responseService.getSingleResponse(null, 403,
                        false, ErrorMessage.USER_EMAIL_NOT_FOUND.getDesc());
            }
            else {
                return responseService.getSingleResponse(null, 500,
                        false, e.toString());
            }
        }
    }
}
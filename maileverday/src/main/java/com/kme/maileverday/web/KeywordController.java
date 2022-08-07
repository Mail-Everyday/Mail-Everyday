package com.kme.maileverday.web;

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

            // 정상적인 접근으로 OK 리턴
            return responseService.getSingleResponse(null, 200, true, ErrorMessage.OK.getDesc());
        } catch (CustomException e) {
            if (e.getCode().equals(ErrorMessage.USER_EMAIL_NOT_FOUND.getCode())) {
                // 유저이메일을 찾을 수 없는 예외로, 해당 하는 메시지 리턴
                return responseService.getSingleResponse(null, 403,
                        false, ErrorMessage.USER_EMAIL_NOT_FOUND.getDesc());
            }
            else {
                // 그외 에러 리턴
                return responseService.getSingleResponse(null, 500,
                        false, e.toString());
            }
        }
    }
}
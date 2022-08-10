package com.kme.maileverday.web;

import com.kme.maileverday.service.KeywordService;
import com.kme.maileverday.service.ResponseService;
import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.CustomMessage;
import com.kme.maileverday.web.dto.keyword.KeywordSaveRequestDto;
import com.kme.maileverday.web.dto.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
            return responseService.getSingleResponse(null, 200, true, CustomMessage.OK.getDesc());
        } catch (CustomException e) {
            return responseService.getSingleResponse(null, e.getHttpCode(), false, e.getMessage());
        }
    }

    @DeleteMapping("api/v1/keywords/{id}")
    @ResponseBody
    public SingleResponse<String> delete(@PathVariable("id") Long id, HttpSession session) {
        try {
            keywordService.delete(id, (String)session.getAttribute("userEmail"));

            return responseService.getSingleResponse(null, 200, true, CustomMessage.OK.getDesc());
        } catch (CustomException e) {
            return responseService.getSingleResponse(null, e.getHttpCode(), false, e.getMessage());
        }
    }
}
package com.kme.maileverday.web;

import com.kme.maileverday.service.KeywordService;
import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.ErrorMessage;
import com.kme.maileverday.web.dto.keyword.KeywordSaveRequestDto;
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

    @PostMapping("/api/v1/keywords")
    @ResponseBody
    public HashMap<String, String> save(@RequestBody KeywordSaveRequestDto requestDto) {
        HashMap<String, String> map = new HashMap<String, String>();

        try {
            keywordService.save(requestDto);
        } catch (CustomException e) {
            if (e.getCode().equals(ErrorMessage.USER_EMAIL_NOT_FOUND.getCode())) {
                map.put("FAILED", e.getMessage());
                return map;
            }
        }
        map.put("SUCCESS", "Y");
        return map;
    }
}
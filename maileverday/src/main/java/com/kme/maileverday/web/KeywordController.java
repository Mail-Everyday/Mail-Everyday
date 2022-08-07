package com.kme.maileverday.web;

import com.kme.maileverday.web.dto.keyword.KeywordSaveRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class KeywordController {
    @PostMapping("/api/v1/keywords")
    @ResponseBody
    public Long save(@RequestBody KeywordSaveRequestDto requestDto) {

    }
}
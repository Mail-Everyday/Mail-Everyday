package com.kme.maileverday.controller;

import com.kme.maileverday.request.KeywordRequestDto;
import com.kme.maileverday.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class KeywordController {


    @Autowired
    private KeywordService keywordService;

    @GetMapping("/mykeyword")
    public String getKeywordList(){
        return ".";
    }

    @GetMapping("/updatekeyword")
    public String toKeyword(){
        return "createKeywordForm";
    }

    @PostMapping("/updatekeyword")
    public String editKeyword(KeywordRequestDto requestDto){
        return "";

    }


}

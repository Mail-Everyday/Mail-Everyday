package com.kme.maileverday.controller;

import com.kme.maileverday.entity.UserKeyword;
import com.kme.maileverday.request.KeywordRequestDto;
import com.kme.maileverday.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class KeywordController {


    @Autowired
    private KeywordService keywordService;

    @GetMapping("/mykeyword")
    public String getKeywordList(HttpSession session, Model model){
        List<UserKeyword> keywordList = keywordService.findMyKeyword(session);
        model.addAttribute("keywordLists", keywordList);
        return "keywordHome";
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

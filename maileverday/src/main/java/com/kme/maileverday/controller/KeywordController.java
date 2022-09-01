package com.kme.maileverday.controller;

import com.kme.maileverday.entity.UserKeyword;
import com.kme.maileverday.request.KeywordRequestDto;
import com.kme.maileverday.response.KeywordResponseDto;
import com.kme.maileverday.service.KeywordService;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/newkeyword")
    private String newkeyword(){
        return "createKeywordForm";
    }
    @PostMapping("/newkeyword")
    private String newkeyword1(HttpSession session, String keyword, String vacationres){

        keywordService.createKeyword(session, keyword, vacationres);
        return "redirect:/mykeyword";
    }


    @GetMapping("/keyword/edit/{id}")
    public String toKeyword(@PathVariable Long id, Model model){

        KeywordResponseDto dto = keywordService.getKeyword(id);
        model.addAttribute("keywordDto", dto);
        return "editKeywordForm";
    }

    @PutMapping("/keyword/edit/{id}")
    public String editKeyword(@PathVariable Long id, KeywordRequestDto requestDto){
        keywordService.editKeyword1(id, requestDto);
        return "redirect:/mykeyword";

    }

    @DeleteMapping("/delete/{id}")
    public String deleteKeyword(@PathVariable Long id){
        System.out.println("삭제");
        keywordService.delKeyword(id);
        return "redirect:/mykeyword";

    }


}

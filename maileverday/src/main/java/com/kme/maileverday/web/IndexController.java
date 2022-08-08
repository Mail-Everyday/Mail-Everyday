package com.kme.maileverday.web;

import com.kme.maileverday.service.KeywordService;
import com.kme.maileverday.service.ResponseService;
import com.kme.maileverday.utility.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final KeywordService keywordService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/keywords")
    public String keywordsList(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");

        try { model.addAttribute("keywords", keywordService.findAllAsc(userEmail)); }
        catch (CustomException e) {
            model.addAttribute("error", e.getCode());
        }
        return "keywords";
    }

    @GetMapping("/keywords/save")
    public String keywordsSave() {
        return "keywords-save";
    }
}

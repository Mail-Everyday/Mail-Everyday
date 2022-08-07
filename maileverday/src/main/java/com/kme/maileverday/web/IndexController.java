package com.kme.maileverday.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/keywords")
    public String keywordsList(HttpSession session) {
        if (session.getAttribute("userEmail") != null) {
            return "keywords";
        }
        else {
            // 잘못된 접근 (로그인이 되지 않은 상태에서 접속 시도)
            return "permissionalert";
        }
    }
}

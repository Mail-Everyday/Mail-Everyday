package com.kme.maileverday.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String Home(HttpSession session){
        return "home";
    }
}

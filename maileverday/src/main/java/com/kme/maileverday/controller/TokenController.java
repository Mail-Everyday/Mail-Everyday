package com.kme.maileverday.controller;

import com.kme.maileverday.request.SignUp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TokenController {

    @RequestMapping("https://accounts.google.com/o/oauth2/v2/auth?client_id=705749547884-7jusq6923nlqt9bos5h8uaiivj9tlj03.apps.googleusercontent.com&redirect_uri=http://localhost:8080&response_type=code&scope=https://mail.google.com/ https://www.googleapis.com/auth/userinfo.profile&access_type=offline")
    public String createCode(HttpServletRequest request){

        String code = request.getParameter("code");
        System.out.println(code);
        return "index";
    }

}

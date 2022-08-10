package com.kme.maileverday.controller;

import com.kme.maileverday.request.SignUp;
import com.kme.maileverday.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${GOOGLEAPICLIENTID}")
    private String clientid;


    @GetMapping("/signup")
    public String createForm(){
        return "createUserForm";
    }

    @PostMapping("/signup")   // signup 뺴고 userform으로도 될듯
    public String create(UserForm form){
        SignUp signUp = new SignUp();
        signUp.setEmail(form.getEmail());
        signUp.setName(form.getName());
        return "redirect:https://accounts.google.com/o/oauth2/v2/auth?"
                + "client_id=" + clientid + "&"
                + "redirect_uri=" + "http://localhost:8080/login" + "&"
                + "response_type=" + "code" + "&"
                + "scope=" + "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile" + "&"
                + "access_type=" + "offline";
    }
}

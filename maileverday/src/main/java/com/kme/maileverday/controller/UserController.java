package com.kme.maileverday.controller;

import com.kme.maileverday.request.SignUp;
import com.kme.maileverday.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String createForm(){
        return "createUserForm";
    }

    @PostMapping("/signup")   // signup 뺴고 userform으로도 될듯
    public String create(UserForm form){
        SignUp signUp = new SignUp();
        signUp.setEmail(form.getEmail());
        signUp.setName(form.getName());

        userService.join(signUp);
        return "redirect:/";
    }
}

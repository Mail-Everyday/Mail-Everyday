package com.kme.maileverday.controller;

import com.kme.maileverday.entity.Token;
import com.kme.maileverday.entity.UserInfo;
import com.kme.maileverday.service.TokenService;
import com.kme.maileverday.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;



    @GetMapping("/login")
    public String createNewUser(@RequestParam(value = "code", required = false) String code){
        Token tokeninfo = tokenService.createToken(code);
        UserInfo userInfo = tokenService.getUserInfo(tokeninfo);
        userService.join(tokeninfo, userInfo);
        return "http://localhost:8080";
    }

}

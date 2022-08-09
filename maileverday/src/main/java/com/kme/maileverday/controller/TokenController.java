package com.kme.maileverday.controller;

import com.kme.maileverday.entity.Token;
import com.kme.maileverday.entity.UserInfo;
import com.kme.maileverday.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class TokenController {

    @Autowired
    private TokenService tokenService;



    @GetMapping("/login")
    @ResponseBody
    public UserInfo createCode(@RequestParam(value = "code", required = false) String code){
        Token tokeninfo = tokenService.createToken(code);
        UserInfo userInfo = tokenService.getUserInfo(tokeninfo);
        return userInfo;
    }

}

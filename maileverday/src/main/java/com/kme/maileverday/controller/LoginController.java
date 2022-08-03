package com.kme.maileverday.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/login")
public class LoginController {

    // 구글 로그인
    @GetMapping("/google")
    public ResponseEntity<?> googleLogin(){

    }
}

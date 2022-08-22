package com.kme.maileverday.controller;

import com.kme.maileverday.request.LoginReq;
import com.kme.maileverday.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${GOOGLEAPICLIENTID}")
    private String clientid;


    @GetMapping("/signup")   // signup 뺴고 userform으로도 될듯
    public String create(){

        return "redirect:https://accounts.google.com/o/oauth2/v2/auth?"
                + "client_id=" + clientid + "&"
                + "redirect_uri=" + "http://localhost:8080/login" + "&"
                + "response_type=" + "code" + "&"
                + "scope=" + "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile" + "&"
                + "access_type=" + "offline";
    }


    @GetMapping("/signin")
    public String toSigninForm(){
        return "loginForm";
    }
    @PostMapping("/signin")
    public String signin(LoginReq loginReq, HttpSession session, RedirectAttributes rttr){

        LoginReq login = userService.userLogin(loginReq);

        String failMessage = "아이디 혹은 비밀번호가 잘못 되었습니다.";

        if (!Objects.equals(login, loginReq)){
            rttr.addFlashAttribute("loginFail", failMessage);
            return "redirect:/signin";
        }
        session.setAttribute("email", loginReq.getEmail());
        session.setAttribute("name", loginReq.getName());

        return "redirect:/";
    }


}

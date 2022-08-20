package com.kme.maileverday.web;

import com.kme.maileverday.service.GoogleUserService;
import com.kme.maileverday.utility.EnvironmentKey;
import com.kme.maileverday.web.dto.googleLogin.LoginServiceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final GoogleUserService googleUserService;

    @GetMapping("/login/google")
    public String loginGoogle(@RequestParam(value = "code", required = false) String authCode, HttpSession session) {
        if (authCode == null) {
            return "redirect:"
                    + "https://accounts.google.com/o/oauth2/v2/auth?"
                    + "client_id=" + EnvironmentKey.getGoogleApiKey() + "&"
                    + "redirect_uri=" + EnvironmentKey.getGoogleApiRedirectUri() + "&"
                    + "scope=" + EnvironmentKey.getGoogleApiScope() + "&"
                    + "response_type=" + "code" + "&"
                    + "access_type=" + "offline";
        }
        else {
            try {
                LoginServiceResponseDto userInfo = googleUserService.login(authCode);
                session.setAttribute("userEmail", userInfo.getUserEmail());
                session.setAttribute("userName", userInfo.getUserName());
            } catch (Exception e) {
                e.printStackTrace();
                return "loginalert";
            }
            return "redirect:"
                    + "/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userEmail");
        session.removeAttribute("userName");
        return "redirect:" + "/";
    }
}
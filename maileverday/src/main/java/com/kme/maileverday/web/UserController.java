package com.kme.maileverday.web;

import com.kme.maileverday.service.GoogleUserService;
import com.kme.maileverday.utility.EnvironmentKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                String userName = googleUserService.login(authCode);
                session.setAttribute("userName", userName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "redirect:"
                    + "/";
        }
    }
}
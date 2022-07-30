package com.kme.maileverday.web;

import com.kme.maileverday.utility.EnvironmentKey;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @GetMapping("/login/google")
    public String loginGoogle(@RequestParam(value = "code", required = false) String authCode) {
        if (authCode == null) {
            return "redirect:"
                    + "https://accounts.google.com/o/oauth2/v2/auth?"
                    + "client_id=" + EnvironmentKey.getGoogleApiKey() + "&"
                    + "redirect_uri=" + EnvironmentKey.getGoogleApiRedirectUri() + "&"
                    + "scope=" + EnvironmentKey.getGoogleApiScope() + "&"
                    + "response_type=" + "code" + "&"
                    + "access_type" + "offline";
        }
        else {
            return "redirect:"
                    + "/";
        }
    }
}
package com.kme.maileverday.web;

import com.kme.maileverday.service.GoogleUserService;
import com.kme.maileverday.service.ResponseService;
import com.kme.maileverday.utility.EnvironmentKey;
import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.CustomMessage;
import com.kme.maileverday.web.dto.googleLogin.LoginServiceResponseDto;
import com.kme.maileverday.web.dto.googleLogin.PhoneUpdateRequestDto;
import com.kme.maileverday.web.dto.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final GoogleUserService googleUserService;
    private final ResponseService responseService;

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

    @PutMapping("/api/v1/phone")
    @ResponseBody
    public SingleResponse<String> phoneUpdate(@RequestBody PhoneUpdateRequestDto request, HttpSession session) {
        try {
            googleUserService.phoneUpdate((String) session.getAttribute("userEmail"), request);
            return responseService.getSingleResponse(null, 200, true, CustomMessage.OK.getDesc());
        } catch (CustomException e) {
            return responseService.getSingleResponse(null, e.getHttpCode(), false, e.getMessage());
        }
    }
}
package com.kme.maileverday.service;

import com.kme.maileverday.controller.TokenController;
import com.kme.maileverday.entity.Token;
import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserInfo;
import com.kme.maileverday.repository.UserRepository;
import com.kme.maileverday.request.SignUp;
import com.kme.maileverday.request.UserDtoReq;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    public void join(Token tokeninfo, UserInfo userInfo){
        validateDuplicateUser(userInfo.getEmail());
        UserDtoReq userDtoReq = UserDtoReq.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .registrationDate(LocalDateTime.now())
                .lastLoginDate(LocalDateTime.now())
                .accessToken(tokeninfo.getAccess_token())
                .refreshToken(tokeninfo.getRefresh_token())
                .lastMailTime(null)
                .build();

        UserEmail userEmail = userDtoReq.toEntity();
        userRepository.save(userEmail);


    }

    private void validateDuplicateUser(String email){
        userRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

}

package com.kme.maileverday.service;

import com.kme.maileverday.controller.TokenController;
import com.kme.maileverday.repository.UserRepository;
import com.kme.maileverday.request.SignUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Autowired
    private TokenController tokenController;

    @Autowired
    private UserRepository userRepository;

    public void join(SignUp signUp){
        validateDuplicateUser(signUp);
        tokenController.createCode();
    }

    private void validateDuplicateUser(SignUp signUp){
        userRepository.findByEmail(signUp.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

}

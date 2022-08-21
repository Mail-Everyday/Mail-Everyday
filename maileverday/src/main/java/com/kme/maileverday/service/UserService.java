package com.kme.maileverday.service;

import com.kme.maileverday.dao.Token;
import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.dao.UserInfo;
import com.kme.maileverday.repository.UserRepository;
import com.kme.maileverday.request.LoginReq;
import com.kme.maileverday.request.UserDtoReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;


@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    public void join(Token tokeninfo, UserInfo userInfo){

        if (validateDuplicateUser(userInfo.getEmail())){

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



    }

    private boolean validateDuplicateUser(String email){
        Optional<UserEmail> userEmail = userRepository.findByEmail(email);
        if (userEmail.isPresent()){
            return false;
        }else{
            return true;
        }

    }


    public LoginReq userLogin(LoginReq loginReq){
        Optional<UserEmail> userEmail = userRepository.findByEmail(loginReq.getEmail());
        System.out.println("시작!");

        if (userEmail.isPresent()){
            if (Objects.equals(userEmail.get().getName(), loginReq.getName())) {
                System.out.println(userEmail.get().getName());
                System.out.println(userEmail.get().getEmail());

                return loginReq;
            } else {
                return null;
            }
        } else{
            return null;
        }

    }

}

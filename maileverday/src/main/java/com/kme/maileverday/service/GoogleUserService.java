package com.kme.maileverday.service;

import com.kme.maileverday.entity.Token;
import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserEmailRepository;
import com.kme.maileverday.utility.GoogleApiHelper;
import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.CustomMessage;
import com.kme.maileverday.web.dto.googleLogin.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class GoogleUserService {
    private final UserEmailRepository userEmailRepository;

    @Transactional
    public LoginServiceResponseDto login(String authCode) throws Exception {
        Token token = Token.getToken(authCode);
        UserInfoResponseGoogleDto userInfo = GoogleApiHelper.getUserInfo(token);

        UserEmail user = userEmailRepository.findByEmail(userInfo.getEmail());
        if (user != null) {
            user.setAccessToken(token.getAccess_token());
            user.setRefreshToken(token.getRefresh_token());
            user.setLastLoginDate(userInfo.getLastLoginDate());
            user.setLastMailTime(userInfo.getLastMailTime());
            userEmailRepository.save(user);
        }
        else {
            user = UserEmail.builder()
                    .email(userInfo.getEmail())
                    .name(userInfo.getName())
                    .registrationDate(userInfo.getRegistrationDate())
                    .lastLoginDate(userInfo.getLastLoginDate())
                    .accessToken(token.getAccess_token())
                    .refreshToken(token.getRefresh_token())
                    .lastMailTime(userInfo.getLastMailTime())
                    .build();
            userEmailRepository.save(user);
        }
        return LoginServiceResponseDto.builder()
                .userEmail(userInfo.getEmail())
                .userName(userInfo.getName())
                .build();
    }

    @Transactional
    public PhoneUpdateResponseDto findPhoneNo(String userEmail) throws CustomException {
        UserEmail user = userEmailRepository.findByEmail(userEmail);

        if (user == null) {
            throw new CustomException(CustomMessage.USER_EMAIL_NOT_FOUND);
        }
        return new PhoneUpdateResponseDto(user);
    }

    @Transactional
    public void phoneUpdate(String userEmail, PhoneUpdateRequestDto request) throws CustomException {
        UserEmail user = userEmailRepository.findByEmail(userEmail);

        if (user == null) {
            throw new CustomException(CustomMessage.USER_EMAIL_NOT_FOUND);
        }
        request.isValidated();  // 유효하지 않은 번호의 경우 예외를 발생시킴
        user.updatePhone(request.getPhoneNo());
    }
}
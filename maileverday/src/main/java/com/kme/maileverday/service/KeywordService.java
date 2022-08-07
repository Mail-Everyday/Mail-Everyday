package com.kme.maileverday.service;

import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserEmailRepository;
import com.kme.maileverday.entity.UserKeywordRepository;
import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.ErrorMessage;
import com.kme.maileverday.web.dto.keyword.KeywordSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KeywordService {
    private final UserEmailRepository userEmailRepository;
    private final UserKeywordRepository userKeywordRepository;

    public void save(KeywordSaveRequestDto requestDto) throws CustomException {
        UserEmail user = userEmailRepository.findByEmail(requestDto.getUserEmail());

        if (user != null) {
            userKeywordRepository.save(requestDto.toEntity(user));
        }
        else {
            throw new CustomException(ErrorMessage.USER_EMAIL_NOT_FOUND);
        }
    }
}

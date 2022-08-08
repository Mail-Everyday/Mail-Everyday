package com.kme.maileverday.service;

import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserEmailRepository;
import com.kme.maileverday.entity.UserKeyword;
import com.kme.maileverday.entity.UserKeywordRepository;
import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.ErrorMessage;
import com.kme.maileverday.web.dto.keyword.KeywordSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class KeywordService {
    private final UserEmailRepository userEmailRepository;
    private final UserKeywordRepository userKeywordRepository;

    @Transactional
    public UserKeyword save(KeywordSaveRequestDto requestDto) throws CustomException {
        UserEmail user = userEmailRepository.findByEmail(requestDto.getUserEmail());

        if (user == null) {
            throw new CustomException(ErrorMessage.USER_EMAIL_NOT_FOUND);
        }
        return userKeywordRepository.save(requestDto.toEntity(user));
    }

    @Transactional
    public List<UserKeyword> findAllAsc(String userEmail) throws CustomException {
        UserEmail user = userEmailRepository.findByEmail(userEmail);

        if (user == null) {
            throw new CustomException(ErrorMessage.USER_EMAIL_NOT_FOUND);
        }
        return userKeywordRepository.findAllAsc(user);
    }
}
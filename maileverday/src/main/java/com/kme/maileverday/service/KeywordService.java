package com.kme.maileverday.service;

import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserEmailRepository;
import com.kme.maileverday.entity.UserKeyword;
import com.kme.maileverday.entity.UserKeywordRepository;
import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.CustomMessage;
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
    public void save(KeywordSaveRequestDto requestDto) throws CustomException {
        UserEmail user = userEmailRepository.findByEmail(requestDto.getUserEmail());

        if (user == null) {
            throw new CustomException(CustomMessage.USER_EMAIL_NOT_FOUND);
        }
        userKeywordRepository.save(requestDto.toEntity(user));
    }

    @Transactional
    public List<UserKeyword> findAllAsc(String userEmail) throws CustomException {
        UserEmail user = userEmailRepository.findByEmail(userEmail);

        if (user == null) {
            throw new CustomException(CustomMessage.USER_EMAIL_NOT_FOUND);
        }
        return userKeywordRepository.findAllAsc(user);
    }

    @Transactional
    public void delete(Long id, String userEmail) throws CustomException {
        UserKeyword keyword = userKeywordRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomMessage.KEYWORD_NOT_FOUND));

        UserEmail user = userEmailRepository.findByEmail(userEmail);
                // 다른 사용자의 키워드를 삭제하려고 접근 하였을 때
                if (keyword.getEmail() != user) {throw new CustomException(CustomMessage.FORBIDDEN);}
        userKeywordRepository.delete(keyword);
    }
}
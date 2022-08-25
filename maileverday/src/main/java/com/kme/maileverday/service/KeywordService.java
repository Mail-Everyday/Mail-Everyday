package com.kme.maileverday.service;

import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserKeyword;
import com.kme.maileverday.repository.KeywordRepository;
import com.kme.maileverday.repository.UserRepository;
import com.kme.maileverday.request.KeywordRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class KeywordService {

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private UserRepository userRepository;

    public List<UserKeyword> findMyKeyword(HttpSession session){

        Optional<UserEmail> userEmail = userRepository.findByEmail((String) session.getAttribute("email"));

        return keywordRepository.findAllByEmail(userEmail.get());
    }

    public void createKeyword(HttpSession session, String keyword, String vacationres){

        UserEmail userEmail = userRepository.findByName((String) session.getAttribute("name"));

        KeywordRequestDto keywordRequestDto = KeywordRequestDto.builder()
                .keyword(keyword)
                .active(true)
                .email(userEmail)
                .filterType((byte) 1)
                .registrationDate(LocalDateTime.now())
                .vacation(false)
                .vacationResponse(vacationres)
                .build();

        UserKeyword userKeyword = keywordRequestDto.toEntity();
        keywordRepository.save(userKeyword);

    }

}

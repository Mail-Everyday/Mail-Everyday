package com.kme.maileverday.service;

import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserKeyword;
import com.kme.maileverday.repository.KeywordRepository;
import com.kme.maileverday.repository.UserRepository;
import com.kme.maileverday.request.KeywordRequestDto;
import com.kme.maileverday.response.KeywordResponseDto;
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

    public KeywordResponseDto getKeyword(Long id){
        Optional<UserKeyword> keywordWrapper = keywordRepository.findById(id);
        UserKeyword keyword = keywordWrapper.get();

        KeywordResponseDto keywordDto = KeywordResponseDto.builder()
                .id(keyword.getId())
                .active(keyword.isActive())
                .keyword(keyword.getKeyword())
                .registrationDate(keyword.getRegistrationDate())
                .vacation(keyword.isVacation())
                .vacationResponse(keyword.getVacationResponse())
                .email(keyword.getEmail())
                .filterType(keyword.getFilterType())
                .build();

        return keywordDto;
    }

    public void editKeyword1(Long id, KeywordRequestDto requestDto){
        Optional<UserKeyword> userKeywordWrapper = keywordRepository.findById(id);
        UserKeyword userKeyword = userKeywordWrapper.get();

        requestDto.setFilterType(userKeyword.getFilterType()); // filterType 개발 전까지 임시
        requestDto.setRegistrationDate(userKeyword.getRegistrationDate());
        requestDto.setEmail(userKeyword.getEmail());

        UserKeyword userKeyword1 = requestDto.toEntity();
        keywordRepository.save(userKeyword1);

    }


    public void delKeyword(Long id){
        keywordRepository.deleteById(id);
    }


}

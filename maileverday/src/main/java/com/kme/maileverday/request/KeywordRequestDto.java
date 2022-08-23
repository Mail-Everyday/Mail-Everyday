package com.kme.maileverday.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeywordRequestDto {

    private String email;

    private String keyword;

    private boolean active;

    private boolean vacation;

    private String vacationResponse;



//    public Keyword toEntity(){
//        Keyword build = Keyword.builder()
//                .keyword(keyword)
//
//                .build()
//    }
}

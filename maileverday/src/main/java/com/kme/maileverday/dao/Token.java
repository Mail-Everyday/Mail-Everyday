package com.kme.maileverday.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private String access_token;
    private Long expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;
    private String id_token;



}

package com.kme.maileverday.service;

import com.kme.maileverday.entity.Token;
import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserInfo;
import com.kme.maileverday.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenService {

    @Autowired
    private UserRepository userRepository;

    @Value("${GOOGLEAPICLIENTID}")
    private String clientid;

    @Value("${GOOGLEAPICLIENTSECRET}")
    private String clientsecret;

    public Token createToken(String code){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientid);
        map.add("client_secret", clientsecret);
        map.add("code", code);
        map.add("grant_type", "authorization_code");
        map.add("redirect_uri", "http://localhost:8080/login");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Token> answer = restTemplate.exchange(url, HttpMethod.POST, entity, Token.class);


        return answer.getBody();
    }

    public UserInfo getUserInfo(Token tokeninfo){

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.add("Authorization", "Bearer " + tokeninfo.getAccess_token());
        System.out.println(headers);
        String url = "https://www.googleapis.com/oauth2/v1/userinfo";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);


        ResponseEntity<UserInfo> answer = restTemplate.exchange(url, HttpMethod.GET, entity, UserInfo.class);
        System.out.println(answer.getBody().getEmailAddresses());
        System.out.println(answer.getBody().getNames());
        return answer.getBody();
    }



}

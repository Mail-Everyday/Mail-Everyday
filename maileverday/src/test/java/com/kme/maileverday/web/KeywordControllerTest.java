package com.kme.maileverday.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserEmailRepository;
import com.kme.maileverday.entity.UserKeyword;
import com.kme.maileverday.entity.UserKeywordRepository;
import com.kme.maileverday.utility.exception.ErrorMessage;
import com.kme.maileverday.web.dto.keyword.KeywordSaveRequestDto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KeywordControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserKeywordRepository keywordRepository;

    @Autowired
    private UserEmailRepository userEmailRepository;

    private MockMvc mvc;

    // MockMvc 설정
    // 키워드 테스트를 위해 TEST 유저 생성
    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();

        userEmailRepository.save(UserEmail.builder()
                .email("TEST@TEST.COM")
                .name("TEST")
                .registrationDate(LocalDateTime.now())
                .lastLoginDate(LocalDateTime.now())
                .build());
    }

    @AfterEach
    public void reset() {
        keywordRepository.deleteAll();
        userEmailRepository.deleteAll();
    }

    @Test
    public void Keyword_정상적으로_등록해본다() throws Exception {
        // given
        String url = "http://localhost:" + port + "/api/v1/keywords";
        String email = "TEST@TEST.COM";
        String keyword = "TESTKEYWORD";
        String vacationMessage = "TESTVACATION";

        KeywordSaveRequestDto requestDto = KeywordSaveRequestDto.builder()
                .keyword(keyword)
                .userEmail(email)
                .vacationMessage(vacationMessage)
                .build();

        // when
        MvcResult result = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                        .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        // then
        assertThat(jsonObject.get("success")).isEqualTo(true);
        assertThat(jsonObject.get("code")).isEqualTo(200L);
        assertThat(jsonObject.get("message")).isEqualTo("OK");

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.get(0).getKeyword()).isEqualTo(keyword);
        assertThat(all.get(0).getEmail().getEmail()).isEqualTo(email);
        assertThat(all.get(0).getVacationResponse()).isEqualTo(vacationMessage);
    }

    @Test
    public void Keyword_예외발생시켜서_등록해본다() throws Exception {
        // given
        String url = "http://localhost:" + port + "/api/v1/keywords";
        String email = "항상실패하는이메일주소 왜냐 이 이메일은 DB에 저장되어 있지가 않거든";
        String keyword = "TESTKEYWORD";
        String vacationMessage = "TESTVACATION";

        KeywordSaveRequestDto requestDto = KeywordSaveRequestDto.builder()
                .keyword(keyword)
                .userEmail(email)
                .vacationMessage(vacationMessage)
                .build();

        // when
        MvcResult result = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        // then
        assertThat(jsonObject.get("success")).isEqualTo(false);
        assertThat(jsonObject.get("code")).isEqualTo(403L);
        assertThat(jsonObject.get("message")).isEqualTo(ErrorMessage.USER_EMAIL_NOT_FOUND.getDesc());

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    public void Keyword_조회해본다() throws Exception {
        // given
        String url = "http://localhost:" + port + "/keywords";
        KeywordSaveRequestDto request = KeywordSaveRequestDto.builder()
                .userEmail("TEST@TEST.COM")
                .keyword("테스트키워드입니다")
                .vacationMessage("테스트키워드메시지입니다")
                .build();
        UserEmail user = userEmailRepository.findByEmail(request.getUserEmail());
        keywordRepository.save(request.toEntity(user));

        // when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userEmail", request.getUserEmail());

        MvcResult result = mvc.perform(get(url)
                        .session(session))
                        .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();

        // then
        assertThat(response.contains(request.getKeyword())).isEqualTo(true);
        assertThat(response.contains(request.getVacationMessage())).isEqualTo(true);
    }
}

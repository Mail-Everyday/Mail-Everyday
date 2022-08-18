package com.kme.maileverday.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserEmailRepository;
import com.kme.maileverday.entity.UserKeyword;
import com.kme.maileverday.entity.UserKeywordRepository;
import com.kme.maileverday.utility.exception.CustomMessage;
import com.kme.maileverday.web.dto.keyword.KeywordSaveRequestDto;
import com.kme.maileverday.web.dto.keyword.KeywordUpdateRequestDto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void Keyword_등록테스트_정상조건() throws Exception {
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
        assertThat(jsonObject.get("code")).isEqualTo((long)CustomMessage.OK.getHttpCode());
        assertThat(jsonObject.get("message")).isEqualTo(CustomMessage.OK.getDesc());

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.get(0).getKeyword()).isEqualTo(keyword);
        assertThat(all.get(0).getEmail().getEmail()).isEqualTo(email);
        assertThat(all.get(0).getVacationResponse()).isEqualTo(vacationMessage);
    }

    @Test
    public void Keyword_등록테스트_가입되어있지않은_회원이등록하려고할때() throws Exception {
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
        assertThat(jsonObject.get("code")).isEqualTo((long)CustomMessage.USER_EMAIL_NOT_FOUND.getHttpCode());
        assertThat(jsonObject.get("message")).isEqualTo(CustomMessage.USER_EMAIL_NOT_FOUND.getDesc());

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    public void Keyword_조회테스트_정상조건() throws Exception {
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

    @Test
    public void Keyword_조회테스트_키워드를_등록한_사용자가_아닌_다른사용자가_페이지에_접근하였을때() throws Exception {
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
        session.setAttribute("userEmail", "새로운유저, 따라서 페이지에 접근하여도 보이는 키워드가 없어야 함");

        MvcResult result = mvc.perform(get(url)
                        .session(session))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();

        // then
        assertThat(response.contains(request.getKeyword())).isEqualTo(false);
        assertThat(response.contains(request.getVacationMessage())).isEqualTo(false);
    }

    @Test
    public void Keyword_삭제테스트_정상조건() throws Exception {
        // given
        KeywordSaveRequestDto request = KeywordSaveRequestDto.builder()
                .userEmail("TEST@TEST.COM")
                .keyword("테스트키워드입니다")
                .vacationMessage("테스트키워드메시지입니다")
                .build();
        UserEmail user = userEmailRepository.findByEmail(request.getUserEmail());
        long idx = keywordRepository.save(request.toEntity(user)).getId();

        String url = "http://localhost:" + port + "/api/v1/keywords/" + idx;

        // when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userEmail", request.getUserEmail());

        MvcResult result = mvc.perform(delete(url)
                        .session(session))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        // then
        assertThat(jsonObject.get("success")).isEqualTo(true);
        assertThat(jsonObject.get("code")).isEqualTo(200L);
        assertThat(jsonObject.get("message")).isEqualTo("OK");

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    public void Keyword_삭제테스트_존재하지않는_키워드의_번호로_삭제시도해본다() throws Exception {
        // given
        KeywordSaveRequestDto request = KeywordSaveRequestDto.builder()
                .userEmail("TEST@TEST.COM")
                .keyword("이 키워드는 삭제되지 않을거에요.")
                .vacationMessage("왜냐하면 존재하지 않는 키워드번호를 삭제시도 해볼거거든요")
                .build();
        UserEmail user = userEmailRepository.findByEmail(request.getUserEmail());
        long idx = keywordRepository.save(request.toEntity(user)).getId();

        String url = "http://localhost:" + port + "/api/v1/keywords/" + (idx + 1);  // idx + 1은 존재하지 않는 키워드번호

        // when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userEmail", request.getUserEmail());

        MvcResult result = mvc.perform(delete(url)
                        .session(session))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        // then
        assertThat(jsonObject.get("success")).isEqualTo(false);
        assertThat(jsonObject.get("code")).isEqualTo((long)CustomMessage.KEYWORD_NOT_FOUND.getHttpCode());
        assertThat(jsonObject.get("message")).isEqualTo(CustomMessage.KEYWORD_NOT_FOUND.getDesc());

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    public void Keyword_삭제테스트_내_키워드가아닌_다른사람의_키워드를_삭제시도할때() throws Exception {
        // given
        KeywordSaveRequestDto request = KeywordSaveRequestDto.builder()
                .userEmail("TEST@TEST.COM")
                .keyword("테스트키워드입니다")
                .vacationMessage("테스트키워드메시지입니다")
                .build();
        UserEmail user = userEmailRepository.findByEmail(request.getUserEmail());
        long idx = keywordRepository.save(request.toEntity(user)).getId();

        String url = "http://localhost:" + port + "/api/v1/keywords/" + idx;

        // when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userEmail", "TEST@TEST.COM이 아닌 악의적인 사용자!!!!!");

        MvcResult result = mvc.perform(delete(url)
                        .session(session))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        // then
        assertThat(jsonObject.get("success")).isEqualTo(false);
        assertThat(jsonObject.get("code")).isEqualTo((long)CustomMessage.FORBIDDEN.getHttpCode());
        assertThat(jsonObject.get("message")).isEqualTo(CustomMessage.FORBIDDEN.getDesc());

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    public void Keyword_수정테스트_메시지수정_정상조건() throws Exception {
        // given
        KeywordSaveRequestDto saveRequest = KeywordSaveRequestDto.builder()
                .userEmail("TEST@TEST.COM")
                .keyword("테스트키워드입니다")
                .vacationMessage("테스트키워드메시지입니다")
                .build();
        UserEmail user = userEmailRepository.findByEmail(saveRequest.getUserEmail());
        long idx = keywordRepository.save(saveRequest.toEntity(user)).getId();

        String url = "http://localhost:" + port + "/api/v1/keywords/" + idx;

        KeywordUpdateRequestDto updateRequest = KeywordUpdateRequestDto.builder()
                .updateRequestType("MESSAGE_UPDATE")
                .vacationMessage("메시지업데이트!")
                .build();

        // when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userEmail", "TEST@TEST.COM");

        MvcResult result = mvc.perform(put(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        // then
        assertThat(jsonObject.get("success")).isEqualTo(true);
        assertThat(jsonObject.get("code")).isEqualTo((long)CustomMessage.OK.getHttpCode());
        assertThat(jsonObject.get("message")).isEqualTo(CustomMessage.OK.getDesc());

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getKeyword()).isEqualTo(saveRequest.getKeyword());
        assertThat(all.get(0).getEmail().getEmail()).isEqualTo(saveRequest.getUserEmail());
        assertThat(all.get(0).getVacationResponse()).isEqualTo(updateRequest.getVacationMessage());
    }

    @Test
    public void Keyword_수정테스트_메시지수정_존재하지않는_키워드번호로_수정시도할때() throws Exception {
        // given
        KeywordSaveRequestDto saveRequest = KeywordSaveRequestDto.builder()
                .userEmail("TEST@TEST.COM")
                .keyword("테스트키워드입니다")
                .vacationMessage("테스트키워드메시지입니다")
                .build();
        UserEmail user = userEmailRepository.findByEmail(saveRequest.getUserEmail());
        long idx = keywordRepository.save(saveRequest.toEntity(user)).getId();

        String url = "http://localhost:" + port + "/api/v1/keywords/" + (idx + 1);  // idx + 1은 존재하지 않는 키워드 번호

        KeywordUpdateRequestDto updateRequest = KeywordUpdateRequestDto.builder()
                .updateRequestType("MESSAGE_UPDATE")
                .vacationMessage("메시지가 업데이트되면 안됩니다. 등록된 키워드 번호가 아닌 존재하지 않는 키워드 번호로 요청을 날리고 있거든요.")
                .build();

        // when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userEmail", "TEST@TEST.COM");

        MvcResult result = mvc.perform(put(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        // then
        assertThat(jsonObject.get("success")).isEqualTo(false);
        assertThat(jsonObject.get("code")).isEqualTo((long)CustomMessage.KEYWORD_NOT_FOUND.getHttpCode());
        assertThat(jsonObject.get("message")).isEqualTo(CustomMessage.KEYWORD_NOT_FOUND.getDesc());

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getKeyword()).isEqualTo(saveRequest.getKeyword());
        assertThat(all.get(0).getEmail().getEmail()).isEqualTo(saveRequest.getUserEmail());
        assertThat(all.get(0).getVacationResponse()).isEqualTo(saveRequest.getVacationMessage());
    }

    @Test
    public void Keyword_수정테스트_메시지수정_내_키워드가아닌_다른사람의_키워드를_수정시도할때() throws Exception {
        // given
        KeywordSaveRequestDto saveRequest = KeywordSaveRequestDto.builder()
                .userEmail("TEST@TEST.COM")
                .keyword("테스트키워드입니다")
                .vacationMessage("테스트키워드메시지입니다")
                .build();
        UserEmail user = userEmailRepository.findByEmail(saveRequest.getUserEmail());
        long idx = keywordRepository.save(saveRequest.toEntity(user)).getId();

        String url = "http://localhost:" + port + "/api/v1/keywords/" + idx;

        KeywordUpdateRequestDto updateRequest = KeywordUpdateRequestDto.builder()
                .updateRequestType("MESSAGE_UPDATE")
                .vacationMessage("메시지가 업데이트되면 안됩니다. 수정권한이 없는 아이디로 수정을 요청하고 있거든요")
                .build();

        // when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userEmail", "저는 등록된 키워드의 소유자가 아닙니다.");

        MvcResult result = mvc.perform(put(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        // then
        assertThat(jsonObject.get("success")).isEqualTo(false);
        assertThat(jsonObject.get("code")).isEqualTo((long)CustomMessage.FORBIDDEN.getHttpCode());
        assertThat(jsonObject.get("message")).isEqualTo(CustomMessage.FORBIDDEN.getDesc());

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getKeyword()).isEqualTo(saveRequest.getKeyword());
        assertThat(all.get(0).getEmail().getEmail()).isEqualTo(saveRequest.getUserEmail());
        assertThat(all.get(0).getVacationResponse()).isEqualTo(saveRequest.getVacationMessage());
    }

    @Test
    public void Keyword_수정테스트_active수정_정상조건() throws Exception {
        // given
        KeywordSaveRequestDto saveRequest = KeywordSaveRequestDto.builder()
                .userEmail("TEST@TEST.COM")
                .keyword("테스트키워드입니다")
                .vacationMessage("테스트키워드메시지입니다")
                .build();
        UserEmail user = userEmailRepository.findByEmail(saveRequest.getUserEmail());
        long idx = keywordRepository.save(saveRequest.toEntity(user)).getId();

        String url = "http://localhost:" + port + "/api/v1/keywords/" + idx;

        KeywordUpdateRequestDto updateRequest = KeywordUpdateRequestDto.builder()
                .updateRequestType("ACTIVE_UPDATE")
                .active(true)
                .build();

        // when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userEmail", "TEST@TEST.COM");

        MvcResult result = mvc.perform(put(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        // then
        assertThat(jsonObject.get("success")).isEqualTo(true);
        assertThat(jsonObject.get("code")).isEqualTo((long)CustomMessage.OK.getHttpCode());
        assertThat(jsonObject.get("message")).isEqualTo(CustomMessage.OK.getDesc());

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getKeyword()).isEqualTo(saveRequest.getKeyword());
        assertThat(all.get(0).getEmail().getEmail()).isEqualTo(saveRequest.getUserEmail());
        assertThat(all.get(0).getVacationResponse()).isEqualTo(saveRequest.getVacationMessage());
        assertThat(all.get(0).isActive()).isEqualTo(true);
        assertThat(all.get(0).isVacation()).isEqualTo(false);
    }

    @Test
    public void Keyword_수정테스트_active수정_내_키워드가아닌_다른사람의_키워드를_수정시도할때() throws Exception {
        // given
        KeywordSaveRequestDto saveRequest = KeywordSaveRequestDto.builder()
                .userEmail("TEST@TEST.COM")
                .keyword("테스트키워드입니다")
                .vacationMessage("테스트키워드메시지입니다")
                .build();
        UserEmail user = userEmailRepository.findByEmail(saveRequest.getUserEmail());
        long idx = keywordRepository.save(saveRequest.toEntity(user)).getId();

        String url = "http://localhost:" + port + "/api/v1/keywords/" + idx;

        KeywordUpdateRequestDto updateRequest = KeywordUpdateRequestDto.builder()
                .updateRequestType("ACTIVE_UPDATE")
                .active(true)
                .build();

        // when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userEmail", "TEST@TEST.COM이 아닌 악의적인 사용자!!");

        MvcResult result = mvc.perform(put(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        // then
        assertThat(jsonObject.get("success")).isEqualTo(false);
        assertThat(jsonObject.get("code")).isEqualTo((long)CustomMessage.FORBIDDEN.getHttpCode());
        assertThat(jsonObject.get("message")).isEqualTo(CustomMessage.FORBIDDEN.getDesc());

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getKeyword()).isEqualTo(saveRequest.getKeyword());
        assertThat(all.get(0).getEmail().getEmail()).isEqualTo(saveRequest.getUserEmail());
        assertThat(all.get(0).getVacationResponse()).isEqualTo(saveRequest.getVacationMessage());
        assertThat(all.get(0).isActive()).isEqualTo(false);
        assertThat(all.get(0).isVacation()).isEqualTo(false);
    }

    @Test
    public void Keyword_수정테스트_vacation수정_정상조건() throws Exception {
        // given
        KeywordSaveRequestDto saveRequest = KeywordSaveRequestDto.builder()
                .userEmail("TEST@TEST.COM")
                .keyword("테스트키워드입니다")
                .vacationMessage("테스트키워드메시지입니다")
                .build();
        UserEmail user = userEmailRepository.findByEmail(saveRequest.getUserEmail());
        long idx = keywordRepository.save(saveRequest.toEntity(user)).getId();

        String url = "http://localhost:" + port + "/api/v1/keywords/" + idx;

        KeywordUpdateRequestDto updateRequest = KeywordUpdateRequestDto.builder()
                .updateRequestType("VACATION_UPDATE")
                .vacation(true)
                .build();

        // when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userEmail", "TEST@TEST.COM");

        MvcResult result = mvc.perform(put(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        // then
        assertThat(jsonObject.get("success")).isEqualTo(true);
        assertThat(jsonObject.get("code")).isEqualTo((long)CustomMessage.OK.getHttpCode());
        assertThat(jsonObject.get("message")).isEqualTo(CustomMessage.OK.getDesc());

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getKeyword()).isEqualTo(saveRequest.getKeyword());
        assertThat(all.get(0).getEmail().getEmail()).isEqualTo(saveRequest.getUserEmail());
        assertThat(all.get(0).getVacationResponse()).isEqualTo(saveRequest.getVacationMessage());
        assertThat(all.get(0).isActive()).isEqualTo(false);
        assertThat(all.get(0).isVacation()).isEqualTo(true);
    }

    @Test
    public void Keyword_수정테스트_vacation수정_내_키워드가아닌_다른사람의_키워드를_수정시도할때() throws Exception {
        // given
        KeywordSaveRequestDto saveRequest = KeywordSaveRequestDto.builder()
                .userEmail("TEST@TEST.COM")
                .keyword("테스트키워드입니다")
                .vacationMessage("테스트키워드메시지입니다")
                .build();
        UserEmail user = userEmailRepository.findByEmail(saveRequest.getUserEmail());
        long idx = keywordRepository.save(saveRequest.toEntity(user)).getId();

        String url = "http://localhost:" + port + "/api/v1/keywords/" + idx;

        KeywordUpdateRequestDto updateRequest = KeywordUpdateRequestDto.builder()
                .updateRequestType("VACATION_UPDATE")
                .vacation(true)
                .build();

        // when
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userEmail", "TEST@TEST.COM이 아닌 악의적인 사용자!!");

        MvcResult result = mvc.perform(put(url)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        // then
        assertThat(jsonObject.get("success")).isEqualTo(false);
        assertThat(jsonObject.get("code")).isEqualTo((long)CustomMessage.FORBIDDEN.getHttpCode());
        assertThat(jsonObject.get("message")).isEqualTo(CustomMessage.FORBIDDEN.getDesc());

        List<UserKeyword> all = keywordRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getKeyword()).isEqualTo(saveRequest.getKeyword());
        assertThat(all.get(0).getEmail().getEmail()).isEqualTo(saveRequest.getUserEmail());
        assertThat(all.get(0).getVacationResponse()).isEqualTo(saveRequest.getVacationMessage());
        assertThat(all.get(0).isActive()).isEqualTo(false);
        assertThat(all.get(0).isVacation()).isEqualTo(false);
    }
}

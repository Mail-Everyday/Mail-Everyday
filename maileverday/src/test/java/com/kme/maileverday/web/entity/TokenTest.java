package com.kme.maileverday.web.entity;

import com.kme.maileverday.entity.Token;
import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenTest {
    @Value("${GOOGLE-API-TEST-ACCESSTOKEN}")
    private String testAccessToken;

    @Value("${GOOGLE-API-TEST-REFRESHTOKEN}")
    private String testRefreshToken;

    @Test
    public void 토큰갱신테스트_리프레쉬토큰사용() {
        // given
        Token token = Token.builder()
                .access_token(testAccessToken)
                .refresh_token(testRefreshToken)
                .build();

        // when
        try {
            token.updateIfExpired();
        } catch (CustomException e) {
            assertThat(e.getCode()).isEqualTo(ErrorMessage.REFRESH_TOKEN_INVALID.getCode());
            assertThat(token.getAccess_token()).isEqualTo(testAccessToken);
            assertThat(token.getRefresh_token()).isEqualTo(testRefreshToken);
            return ;
        }

        // then
        assertThat(token.getAccess_token()).isNotEqualTo(testAccessToken);
        assertThat(token.getRefresh_token()).isEqualTo(testRefreshToken);
    }
}
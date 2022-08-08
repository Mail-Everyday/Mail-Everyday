package com.kme.maileverday.web;

import com.kme.maileverday.service.KeywordService;
import com.kme.maileverday.service.ResponseService;
import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final KeywordService keywordService;
    private final ResponseService responseService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/keywords")
    public String keywordsList(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail != null) {
            // 모델로 넘겨줄 키 값
            final String responseKey = "ListResponse";

            try {
                // 서비스 정상 작동 시
                // 유저키워드 엔티티 (List), 200 (상태코드), true (성공여부), OK (메시지) 를 내려줌
                model.addAttribute(responseKey,
                        responseService.getListResponse(keywordService.findAllAsc(userEmail), 200,
                                true, ErrorMessage.OK.getDesc()));
            } catch (CustomException e) {
                // 서비스 예외 발생 시
                // 유저키워드 엔티티 (null), (상태코드), false (성공여부), (메시지) 를 내려줌
                if (e.getCode().equals(ErrorMessage.USER_EMAIL_NOT_FOUND.getCode())) {
                    model.addAttribute(responseKey,
                            responseService.getListResponse(null, 403, false, ErrorMessage.USER_EMAIL_NOT_FOUND.getDesc()));
                }
                else {
                    model.addAttribute(responseKey,
                            responseService.getListResponse(null, 500, false, e.toString()));
                }
            }
            return "keywords";
        }
        else {
            // 잘못된 접근 (로그인이 되지 않은 상태에서 접속 시도)
            return "permissionalert";
        }
    }

    @GetMapping("/keywords/save")
    public String keywordsSave(HttpSession session) {
        if (session.getAttribute("userEmail") != null) {
            return "keywords-save";
        }
        else {
            // 잘못된 접근 (로그인이 되지 않은 상태에서 접속 시도)
            return "permissionalert";
        }
    }
}

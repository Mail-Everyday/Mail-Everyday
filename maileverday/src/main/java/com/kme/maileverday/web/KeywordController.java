package com.kme.maileverday.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class KeywordController {
    @PostMapping("/api/v1/keywords")
    public Long save(@RequestBody)
}

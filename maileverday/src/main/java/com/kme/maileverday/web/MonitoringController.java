package com.kme.maileverday.web;

import com.kme.maileverday.service.MonitoringService;
import com.kme.maileverday.utility.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class MonitoringController {
    private final MonitoringService monitoringService;

    @GetMapping("/test/monitoring")
    public void test() {
        monitoringService.monitoring();
    }
}

package com.kme.maileverday.service;

import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserEmailRepository;
import com.kme.maileverday.utility.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MonitoringService {
    private final UserEmailRepository userEmailRepository;

    @Transactional
    public void test2() {
        List<UserEmail> all = userEmailRepository.findAll();

        for (int i = 0; i < all.size(); i++) {
            // 새로운 메일이 도착했다면
            if (receivedNewMail()) {
                // 새로운 메일중에 설정된 키워드가 포함되어 있다면
                if (containKeyword) {
                    // psudo code
                    // sendAlarm();
                }
            }
        }
    }

    @Transactional
    public void test() {
        List<UserEmail> all = userEmailRepository.findAll();

        for (int i = 0; i < all.size(); i++) {
            System.out.println(all.get(i).getName() + " " + all.get(i).getLastMailTime());
            System.out.println(exchangeToLocalDateTime(all.get(i).getLastMailTime()));
        }
    }

    private boolean receivedNewMail(UserEmail userEmail) {

    }
}
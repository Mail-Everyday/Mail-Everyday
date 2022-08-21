package com.kme.maileverday.service;

import com.kme.maileverday.entity.*;
import com.kme.maileverday.utility.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MonitoringService {
    private final UserEmailRepository userEmailRepository;
    private final UserKeywordRepository userKeywordRepository;
    private final EmailHandler emailHandler;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void test() throws CustomException {
        System.out.println("Monitoring available");
        List<UserEmail> all = userEmailRepository.findAll();

        for (int i = 0; i < all.size(); i++) {
            List<Email> emails = emailHandler.getNewEmails(all.get(i));

            if (emails.size() > 0) {
                List<UserKeyword> keywords = userKeywordRepository.findActivedAllAsc(all.get(i));

                if (keywords.size() > 0) {
                    for (int j = 0; j < emails.size(); j++) {
                        for (int k = 0; k < keywords.size(); k++) {
                            if (emails.get(j).getSubject().contains(keywords.get(k).getKeyword())) {
                                System.out.println("Subject: " + emails.get(j).getSubject() + " Date: " + emails.get(j).getDate());
                            }
                        }
                    }
                }

                // 가장 마지막 메시지 도착날짜 업데이트 쿼리
                ZoneId zoneId = ZoneId.of("Asia/Seoul");
                ZonedDateTime zdtTime = emails.get(0).getDate().atZone(zoneId);
                all.get(i).updateLastMailTime(DateTimeFormatter.RFC_1123_DATE_TIME.format(zdtTime));
            }
        }
    }
}
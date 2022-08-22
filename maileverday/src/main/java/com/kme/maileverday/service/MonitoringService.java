package com.kme.maileverday.service;

import com.kme.maileverday.entity.*;
import com.kme.maileverday.utility.LogColorHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final NotificationService notificationService;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void monitoring() {
        System.out.println(LogColorHelper.blue + "> Scheduled - Monitoring " + LocalDateTime.now() + LogColorHelper.exit);
        List<UserEmail> all = userEmailRepository.findAll();

        for (int i = 0; i < all.size(); i++) {
            List<Email> emails = emailHandler.getNewEmails(all.get(i));

            if (emails.size() > 0) {
                List<UserKeyword> keywords = userKeywordRepository.findActivedAllAsc(all.get(i));

                if (keywords.size() > 0) {
                    for (int j = 0; j < emails.size(); j++) {
                        for (int k = 0; k < keywords.size(); k++) {
                            if (emails.get(j).getSubject().contains(keywords.get(k).getKeyword())) {
                                System.out.println(LogColorHelper.green +
                                        "Subject: " + emails.get(j).getSubject() + " Date: " + emails.get(j).getDate() + LogColorHelper.exit);

                                // SMS 발송시도
                                try {notificationService.sendSMS(emails.get(j), all.get(i).getPhone());}
                                catch (Exception e) {
                                    System.out.println(LogColorHelper.red + "> SMS Send Failed!" + LogColorHelper.exit);
                                    e.printStackTrace();
                                }
                                // 다음 이메일로
                                break;
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
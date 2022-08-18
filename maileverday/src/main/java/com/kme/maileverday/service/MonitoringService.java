package com.kme.maileverday.service;

import com.kme.maileverday.entity.Email;
import com.kme.maileverday.entity.EmailHandler;
import com.kme.maileverday.entity.UserEmail;
import com.kme.maileverday.entity.UserEmailRepository;
import com.kme.maileverday.utility.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MonitoringService {
    private final UserEmailRepository userEmailRepository;
    private final EmailHandler emailHandler;

    @Transactional
    public void test() {
        List<UserEmail> all = userEmailRepository.findAll();

        for (int i = 0; i < all.size(); i++) {
            List<Email> emails = emailHandler.getNewEmails(all.get(i));

            for (int j = 0; j < emails.size(); j++) {
                System.out.println("Subject: " + emails.get(j).getSubject() + " Date: " + emails.get(j).getDate());
            }
        }
    }
}
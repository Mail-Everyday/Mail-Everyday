package com.kme.maileverday.service;

import com.kme.maileverday.entity.Email;
import com.kme.maileverday.utility.NCloudApiHelper;
import com.kme.maileverday.web.dto.ncloudsens.SendSMSResponseDto;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public SendSMSResponseDto sendSMS(Email email, String to) throws Exception {
        return NCloudApiHelper.sendSMS(email.getSubject(), to);
    }
}

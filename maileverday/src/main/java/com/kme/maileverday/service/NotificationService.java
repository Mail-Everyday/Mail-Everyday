package com.kme.maileverday.service;

import com.kme.maileverday.entity.Email;
import com.kme.maileverday.utility.NCloudApiHelper;
import com.kme.maileverday.web.dto.ncloudsens.SendSMSResponseDto;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public SendSMSResponseDto sendSMS(Email email, String to) throws Exception {
        SendSMSResponseDto response = NCloudApiHelper.sendSMS(email.getSubject(), to);
        System.out.println("> SMS Send Success!\ncode: " + response.getStatusCode() + "\nstatus: " + response.getStatusName()
                + "\nrequest id: " + response.getRequestId() + "\nrequest time: " + response.getRequestTime());

        return response;
    }
}

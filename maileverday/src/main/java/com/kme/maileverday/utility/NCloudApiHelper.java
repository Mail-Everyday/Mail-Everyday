package com.kme.maileverday.utility;

import com.kme.maileverday.web.dto.ncloudsens.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;

@Component
public class NCloudApiHelper {
    public static String makeSignature(String method, String url, String timestamp) throws Exception {
        String space = " ";
        String newLine = "\n";

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(EnvironmentKey.getNaverCloudAccessKey())
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(EnvironmentKey.getNaverCloudSecretKey().getBytes("UTF-8"),
                "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    public static SendSMSResponseDto sendSMS(String content, String to) throws Exception {
        final String apiUrl = "https://sens.apigw.ntruss.com/sms/v2/services/"
                + EnvironmentKey.getNaverCloudSMSServiceId()
                + "/messages";
        final String timestamp = Long.toString(System.currentTimeMillis());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", timestamp);
        headers.set("x-ncp-iam-access-key", EnvironmentKey.getNaverCloudAccessKey());
        headers.set("x-ncp-apigw-signature-v2", makeSignature("POST",
                "/sms/v2/services/" + EnvironmentKey.getNaverCloudSMSServiceId() + "/messages", timestamp));

        RestTemplate restTemplate = new RestTemplate();

        List<MessagesObject> msgObjects = new ArrayList<>();
        msgObjects.add(MessagesObject.builder()
                .to(to).build());

        SendSMSRequestDto requestBody = SendSMSRequestDto.builder()
                .type(SMSTypeEnum.SMS)
                .contentType(SMSContentType.COMM)
                .countryCode("82")
                .from(EnvironmentKey.getNaverCloudFromPhoneNo())
                .content(content)
                .messages(msgObjects).build();

        HttpEntity<SendSMSRequestDto> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<SendSMSResponseDto> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, SendSMSResponseDto.class);
        return response.getBody();
    }
}

package com.kme.maileverday.utility;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EnvironmentKey {
    @Getter
    private static String googleApiKey;

    @Getter
    private static String googleApiSecret;

    @Getter
    private static String googleApiRedirectUri;

    @Getter
    private static String googleApiScope;

    @Getter
    private static String dateKeyName;

    @Getter
    private static String naverCloudAccessKey;

    @Getter
    private static String naverCloudSecretKey;

    @Getter
    private static String naverCloudSMSServiceId;

    @Getter
    private static String naverCloudFromPhoneNo;

    @Value("${GOOGLE-API-KEY}")
    private void setGoogleApiKey(String value) {
        googleApiKey = value;
    }

    @Value("${GOOGLE-API-SECRET}")
    private void setGoogleApiSecret(String value) {
        googleApiSecret = value;
    }

    @Value("${GOOGLE-API-REDIRECT-URI}")
    private void setGoogleApiRedirectUri(String value) {
        googleApiRedirectUri = value;
    }

    @Value("${GOOGLE-API-SCOPE}")
    private void setGoogleApiScope(String value) {
        googleApiScope = value;
    }

    @Value("${GOOGLE-API-DATETIME-KEY-NAME}")
    private void setDateKeyName(String value) {
        dateKeyName = value;
    }

    @Value("${NCLOUD-ACCESS-KEY}")
    private void setNaverCloudAccessKey(String value) {
        naverCloudAccessKey = value;
    }

    @Value("${NCLOUD-SECRET-KEY}")
    private void setNaverCloudSecretKey(String value) {
        naverCloudSecretKey = value;
    }

    @Value("${NCLOUD-SMS-SERVICE-ID}")
    private void setNaverCloudSMSServiceId(String value) {
        naverCloudSMSServiceId = value;
    }

    @Value("${NCLOUD-SMS-FROM-PHONE-NO}")
    private void setNaverCloudFromPhoneNo(String value) {
        naverCloudFromPhoneNo = value;
    }
}
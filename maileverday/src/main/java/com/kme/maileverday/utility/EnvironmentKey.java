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
}

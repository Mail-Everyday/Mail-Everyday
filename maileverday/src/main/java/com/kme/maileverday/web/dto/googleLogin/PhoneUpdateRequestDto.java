package com.kme.maileverday.web.dto.googleLogin;

import com.kme.maileverday.utility.exception.CustomException;
import com.kme.maileverday.utility.exception.CustomMessage;
import lombok.Getter;

@Getter
public class PhoneUpdateRequestDto {
    private String phoneNo;

    public void isValidated() throws CustomException {
        String regExp = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$";

        if (!phoneNo.matches(regExp))
            throw new CustomException(CustomMessage.PHONE_NUMBER_FORMAT_INVALIDATED);
    }
}

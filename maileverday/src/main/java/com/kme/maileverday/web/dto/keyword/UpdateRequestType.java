package com.kme.maileverday.web.dto.keyword;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UpdateRequestType {
    MESSAGE_UPDATE,
    ACTIVE_UPDATE,
    VACATION_UPDATE,
    UNKNOWN_REQUEST;

    @JsonCreator
    public static UpdateRequestType from(String value) {
        for (UpdateRequestType it : UpdateRequestType.values()) {
            if (it.name().equals(value)) {
                return it;
            }
        }
        return UNKNOWN_REQUEST;
    }
}
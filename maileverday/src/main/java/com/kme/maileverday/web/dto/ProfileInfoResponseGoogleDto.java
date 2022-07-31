package com.kme.maileverday.web.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ProfileInfoResponseGoogleDto {
    List<ProfileInfoNamesDto> names;
}
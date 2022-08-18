package com.kme.maileverday.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Email {
    private String id;
    private String from;
    private String subject;
    private String snippet;
    private LocalDateTime date;

    @Builder
    Email(String id, String from, String subject, String snippet, LocalDateTime date) {
        this.id = id;
        this.from = from;
        this.subject = subject;
        this.snippet = snippet;
        this.date = date;
    }
}

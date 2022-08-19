package com.kme.maileverday.service;

import com.kme.maileverday.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeywordService {

    @Autowired
    private KeywordRepository keywordRepository;


}

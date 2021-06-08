package com.semmet.personattribute.personattribute.controller;

import java.util.Map;

import com.semmet.personattribute.personattribute.repository.EntitiesRepository;
import com.semmet.personattribute.personattribute.repository.KeyPhrasesRepository;
import com.semmet.personattribute.personattribute.repository.UserEntityMappingsRepository;
import com.semmet.personattribute.personattribute.repository.UserKeyPhrasesMappingsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/text-data")
public class TextDataController {
    
    @Autowired
    private EntitiesRepository entitiesRepository;
    @Autowired
    private KeyPhrasesRepository keyPhrasesRepository;
    @Autowired
    private UserEntityMappingsRepository userEntityMappingsRepository;
    @Autowired
    private UserKeyPhrasesMappingsRepository userKeyPhrasesMappingsRepository;

    @PostMapping(produces = "application/json", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public void addTextData(@RequestParam Map<String, String> body) {
        var text = body.get("text-data");
    }
}

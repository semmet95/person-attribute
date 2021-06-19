package com.semmet.personattribute.personattribute.controller;

import com.semmet.personattribute.personattribute.model.UserKeyPhraseMappings;
import com.semmet.personattribute.personattribute.repository.UserKeyPhrasesMappingsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-keyphrase-mappings")
public class UserKeyPhraseMappingsController {
    
    @Autowired
    private UserKeyPhrasesMappingsRepository userKeyPhrasesMappingsRepository;

    @GetMapping
    public Iterable<UserKeyPhraseMappings> getUserKeyPhraseMappings(@RequestParam String userId) {
        return userKeyPhrasesMappingsRepository.findAllByUser_UserId(Long.parseLong(userId));
    }
}

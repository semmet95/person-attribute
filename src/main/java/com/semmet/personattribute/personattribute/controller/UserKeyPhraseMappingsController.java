package com.semmet.personattribute.personattribute.controller;

import com.semmet.personattribute.personattribute.model.UserKeyPhraseMappings;
import com.semmet.personattribute.personattribute.repository.UserKeyPhrasesMappingsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserKeyPhraseMappingsController class receives get requests on the path /user-keyphrase-mappings
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@RestController
@RequestMapping("/user-keyphrase-mappings")
public class UserKeyPhraseMappingsController {
    
    @Autowired
    private UserKeyPhrasesMappingsRepository userKeyPhrasesMappingsRepository;

    /**
     * This method receives GET requests on the path /user-keyphrase-mappings
     * and returns all the key phrases associated with a user described by the
     * given userId
     * @see UserKeyPhraseMappings
     * 
     * @param userId userId value for the user that you want to get associated key phrases with
     * @return list of all the key phrases mapped to a userId
     */

    @GetMapping
    public Iterable<UserKeyPhraseMappings> getUserKeyPhraseMappings(@RequestParam String userId) {
        return userKeyPhrasesMappingsRepository.findAllByUser_UserId(Long.parseLong(userId));
    }
}

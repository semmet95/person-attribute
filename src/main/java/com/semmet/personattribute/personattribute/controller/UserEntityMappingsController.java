package com.semmet.personattribute.personattribute.controller;

import com.semmet.personattribute.personattribute.model.UserEntityMappings;
import com.semmet.personattribute.personattribute.repository.UserEntityMappingsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-entity-mappings")
public class UserEntityMappingsController {
    
    @Autowired
    private UserEntityMappingsRepository userEntityMappingsRepository;

    @GetMapping
    public Iterable<UserEntityMappings> getUserEntityMappings(@RequestParam String userId) {
        return userEntityMappingsRepository.findAllByUser_UserId(Long.parseLong(userId));
    }
}

package com.semmet.personattribute.personattribute.controller;

import com.semmet.personattribute.personattribute.model.UserEntityMappings;
import com.semmet.personattribute.personattribute.repository.UserEntityMappingsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserEntityMappingsController class receives get requests on the path /user-entity-mappings.
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@RestController
@RequestMapping("/user-entity-mappings")
public class UserEntityMappingsController {
    
    @Autowired
    private UserEntityMappingsRepository userEntityMappingsRepository;

    /**
     * This method receives GET requests on the path /user-entity-mappings
     * and returns all the entities associated with a user described by the
     * given userId
     * @see UserEntityMappings
     * 
     * @param userId userId value for the user that you want to get associated entities with
     * @return list of all the entities mapped to a userId
     */
    
    @GetMapping
    public Iterable<UserEntityMappings> getUserEntityMappings(@RequestParam String userId) {
        return userEntityMappingsRepository.findAllByUser_UserId(Long.parseLong(userId));
    }
}

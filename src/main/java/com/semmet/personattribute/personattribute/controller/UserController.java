package com.semmet.personattribute.personattribute.controller;

import java.util.Map;

import com.semmet.personattribute.personattribute.model.Users;
import com.semmet.personattribute.personattribute.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController class receives get and post requests on the path /users.
 * 
 * @author Amit Singh
 * @version 0.1
 * @since 2021-06-23
 */

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    /**
     * This method receives GET requests on the path /users
     * @see Users
     * 
     * @return list of all the users stored in the database
     */

    @GetMapping(produces = "application/json")
    public Iterable<Users> getUsers() {
        return userRepository.findAll();
    }

    /**
     * This method receives POST requests on the path /users
     * and creates new users in the database from the provided body
     * @see Users
     * 
     * @param body the body posted containing the user metadata
     * It should be of type {@code application/x-www-form-urlencoded;charset=UTF-8}  
     * @return the newly created user
     */

    @PostMapping(produces = "application/json", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public Users addUser(@RequestParam Map<String, String> body) {
        
        var user = new Users();
        user.setAge(Integer.parseInt(body.get("age")));
        user.setCity(body.get("city"));
        user.setGender(body.get("gender"));
        user.setName(body.get("name"));
        user.setPhoneNum(body.get("phoneNum"));
        //user.setUserId(Long.parseLong(body.get("userId")));
        
        userRepository.save(user);

        return userRepository.findByUserId(Long.parseLong(body.get("userId"))).get(0);
    }
}

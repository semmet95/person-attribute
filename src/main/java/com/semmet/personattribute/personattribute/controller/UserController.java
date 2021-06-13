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

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping(produces = "application/json")
    public Iterable<Users> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping(produces = "application/json", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public Users addUser(@RequestParam Map<String, String> body) {
        
        var user = new Users();
        user.setAge(Integer.parseInt(body.get("age")));
        user.setCity(body.get("city"));
        user.setGender(body.get("gender"));
        user.setName(body.get("name"));
        user.setPhoneNum(body.get("phoneNum"));
        user.setUserId(Long.parseLong(body.get("userId")));
        
        userRepository.save(user);

        return userRepository.findByUserId(Long.parseLong(body.get("userId"))).get(0);
    }
}

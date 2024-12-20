package com.example.security1.Controller;

import com.example.security1.Service.UserService;
import com.example.security1.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserConroller {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public Users register(@RequestBody Users user){
        service.register(user);
        return user;
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user){
        return service.verify(user);
    }

}

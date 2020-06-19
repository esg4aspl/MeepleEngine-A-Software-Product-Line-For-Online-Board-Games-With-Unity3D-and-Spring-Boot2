package com.meepleengine.meepleserver.controller;

import com.meepleengine.meepleserver.model.dto.TokenDTO;
import com.meepleengine.meepleserver.model.request.UserRequest;
import com.meepleengine.meepleserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void register(@RequestBody UserRequest userRequest) {
        userService.register(userRequest);
    }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody UserRequest userRequest) {
        return userService.login(userRequest);
    }

}

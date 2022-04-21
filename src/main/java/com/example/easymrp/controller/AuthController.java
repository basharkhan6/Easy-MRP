package com.example.easymrp.controller;

import com.example.easymrp.exception.badrequest.BadRequestException;
import com.example.easymrp.model.auth.User;
import com.example.easymrp.model.dto.SignupRequest;
import com.example.easymrp.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public void registerUser(@RequestBody @Valid SignupRequest signupRequest) {
        if (userService.isExist(signupRequest.getUsername())) {
            throw new BadRequestException("Username already exist!!");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(signupRequest.getPassword());
        userService.createNew(user);
    }

}
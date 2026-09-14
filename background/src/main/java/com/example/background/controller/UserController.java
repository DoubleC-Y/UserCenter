package com.example.background.controller;

import com.example.background.model.domain.User;
import com.example.background.model.dto.UserLoginRequest;
import com.example.background.model.dto.UserRegisterRequest;
import com.example.background.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (userAccount == null || userPassword == null || checkPassword == null){
            return null;
        }
        return userService.register(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (userAccount == null || userPassword == null) {
            return null;
        }
        return userService.login(userAccount, userPassword, httpServletRequest);
    }

}

package com.example.background.controller;

import com.example.background.model.domain.User;
import com.example.background.model.dto.UserLoginRequest;
import com.example.background.model.dto.UserRegisterRequest;
import com.example.background.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.background.constants.UserConstant.USER_LOGIN_STATE;
import static com.example.background.constants.UserConstant.USER_ROLE_ADMIN;

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

    @GetMapping("/search")
    public List<User> searchUser(@RequestParam String name, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }
        return userService.searchUser(name);
    }

    @PostMapping("/delete")
    public boolean delete(@RequestParam Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 判断当前会话里的登录用户是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 未登录时取不到属性；instanceof 同时挡掉类型不符的旧 session 数据
        if (!(request.getSession().getAttribute(USER_LOGIN_STATE) instanceof User loginUser)) {
            return false;
        }
        // role 是包装类型，为 null 时直接 != 会抛 NPE，故先判空再比数值
        return loginUser.getRole() != null && loginUser.getRole() == USER_ROLE_ADMIN;
    }
}

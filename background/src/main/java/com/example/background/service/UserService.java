package com.example.background.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.example.background.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author 10611
* @description 针对表【user】的数据库操作Service
* @createDate 2026-09-13 22:40:07
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return 新用户id
     */
    long register(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return 登录结果
     */
    User login(String userAccount, String userPassword, HttpServletRequest httpServletRequest);

}

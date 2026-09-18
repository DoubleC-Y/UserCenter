package com.example.background.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.example.background.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

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

    /**
     * 用户数据脱敏：拷出一个只含可外发字段的新对象
     * @param user 原始用户
     * @return 脱敏后的用户，入参为 null 时返回 null
     */
    User desensitizeUser(User user);

    /**
     * 搜索用户
     * @param name 姓名关键字，为空时返回全量
     * @return 脱敏后的用户列表
     */
    List<User> searchUser(String name);
}

package com.example.background.service;

import com.example.background.model.domain.User;
import com.example.background.utils.PasswordUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceTest {
    @Resource
    UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        // 账号列 NOT NULL 无默认值、唯一且仅允许数字，用毫秒时间戳保证每次执行不冲突
        user.setAccount(String.valueOf(System.currentTimeMillis()));
        user.setName("test");
        user.setAge(18);
        user.setEmail("test@example.com");
        user.setPhone("12345678901");
        user.setAvatar("avatar.jpg");
        user.setIntro("This is a test user.");
        // 密码列同样 NOT NULL 且无默认值，必须写入摘要串
        user.setPassword(PasswordUtil.encrypt("12345678"));
        boolean result = userService.save(user);
        System.out.println(user.getId());
        assertTrue(result);
    }

    @Test
    void register() {
        String testAccount = "000003";
        String testPassword = "12345678";
        String testCheckPassword = "12345678";
        long result = userService.register(testAccount, testPassword, testCheckPassword);
        assertTrue(result > 0);
        System.out.println(result);
    }
}
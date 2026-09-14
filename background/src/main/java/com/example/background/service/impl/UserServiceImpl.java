package com.example.background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.background.mapper.UserMapper;
import com.example.background.model.domain.User;
import com.example.background.service.UserService;
import com.example.background.utils.PasswordUtil;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
* @author 10611
* @description 针对表【user】的数据库操作Service实现
* @createDate 2026-09-13 22:40:07
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 账号规则：仅数字，长度 4~20
     * 与 user.account 的 CHECK 约束（^[0-9]+$）以及 varchar(20) 列宽保持一致
     */
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("[0-9]{4,20}");

    @Override
    public long register(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        // 非空校验：先挡掉 null 与纯空白，后续的 length / matches 才能直接调用
        if (userAccount == null || userPassword == null || checkPassword == null
                || userAccount.isBlank() || userPassword.isBlank()) {
            return -1;
        }
        // 账号格式校验：长度与字符合并成一次正则，不合法就没必要再往后走
        if (!ACCOUNT_PATTERN.matcher(userAccount).matches()) {
            return -1;
        }
        // 两次密码一致性校验
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        // 密码长度校验
        if (userPassword.length() < 8) {
            return -1;
        }
        // 账号重复校验：唯一一次数据库访问，放在所有纯内存校验之后
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", userAccount);
        if (this.baseMapper.exists(queryWrapper)) {
            return -1;
        }
        // 2. 密码加密：SHA-256 + 随机盐，落库的只是摘要串，完整明文不会进入数据库
        String encryptedPassword = PasswordUtil.encrypt(userPassword);

        // 3. 新用户入库，主键由 MyBatis-Plus 雪花算法生成
        User user = new User();
        user.setAccount(userAccount);
        user.setPassword(encryptedPassword);
        if (!this.save(user)) {
            return -1;
        }
        return user.getId();
    }
}





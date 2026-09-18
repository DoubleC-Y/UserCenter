package com.example.background.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.example.background.mapper.UserMapper;
import com.example.background.model.domain.User;
import com.example.background.service.UserService;
import com.example.background.utils.PasswordUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

import static com.example.background.constants.UserConstant.USER_LOGIN_STATE;

/**
* @author 10611
* @description 针对表【user】的数据库操作Service实现
* @createDate 2026-09-13 22:40:07
*/
@Service
@Slf4j
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
        // 账号和密码长度校验
        if (userAccount.length() < 4 || userPassword.length() < 8) {
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

        // 3. 新用户入库，主键由 MyBatis-Plus 雪花算法生成；
        //    role / enabled / workStatus 不赋值，走数据库默认值 0 / 1 / 1，
        //    自助注册拿不到管理员角色
        User user = new User();
        user.setAccount(userAccount);
        user.setPassword(encryptedPassword);
        if (!this.save(user)) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User login(String userAccount, String userPassword, HttpServletRequest httpServletRequest) {
        // 1. 校验，与注册的校验逻辑基本一致
        if (userAccount == null || userPassword == null || userAccount.isBlank() || userPassword.isBlank()) {
            return null;
        }
        // 账号格式校验：长度与字符合并成一次正则，不合法就没必要再往后走
        if (!ACCOUNT_PATTERN.matcher(userAccount).matches()) {
            return null;
        }
        // 账号和密码长度校验
        if (userAccount.length() < 4 || userPassword.length() < 8) {
            return null;
        }
        // 2. 查询用户：查询条件只用 account，密码绝不能参与查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", userAccount);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 3. 密码校验：摘要里的盐是随机的，同一明文每次密文都不同，
        //    所以必须用 matches 重新计算后比对，不能把明文加密再拿去查库
        if (user == null || !PasswordUtil.matches(userPassword, user.getPassword())) {
            log.info("登录失败，账号或密码错误");
            return null;
        }
        // 4. 数据脱敏：只回传可外发字段，不含密码摘要
        User desensitizedUser = desensitizeUser(user);
        // 5. 记录用户登录态：存脱敏后的对象，避免密码摘要被带进 session
        httpServletRequest.getSession().setAttribute(USER_LOGIN_STATE, desensitizedUser);
        return desensitizedUser;
    }

    @Override
    public User desensitizeUser(User user) {
        if (user == null) {
            return null;
        }
        // 白名单式拷贝：只列允许外发的字段，password / updateTime / deleted 一律不带；
        // role 不敏感且鉴权要从 session 里取，所以必须保留
        User desensitizedUser = new User();
        desensitizedUser.setId(user.getId());
        desensitizedUser.setName(user.getName());
        desensitizedUser.setAccount(user.getAccount());
        desensitizedUser.setAge(user.getAge());
        desensitizedUser.setPhone(user.getPhone());
        desensitizedUser.setEmail(user.getEmail());
        desensitizedUser.setAvatar(user.getAvatar());
        desensitizedUser.setIntro(user.getIntro());
        desensitizedUser.setEnabled(user.getEnabled());
        desensitizedUser.setWorkStatus(user.getWorkStatus());
        desensitizedUser.setRole(user.getRole());
        desensitizedUser.setCreateTime(user.getCreateTime());
        return desensitizedUser;
    }

    @Override
    public List<User> searchUser(String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        // 列表接口逐条脱敏：这里漏一次等于把整批用户的密码摘要一起发出去
        return this.list(queryWrapper).stream()
                .map(this::desensitizeUser)
                .toList();
    }
}





package com.example.background.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 登录账号:仅数字
     */
    private String account;

    /**
     * 密码:盐$SHA-256摘要,不存明文
     */
    private String password;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 个人简介
     */
    private String intro;

    /**
     * 是否启用:1启用,0停用
     */
    private Integer enabled;

    /**
     * 人员状态:1在职,2离职
     */
    private Integer workStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除:0未删除,1已删除
     */
    @TableLogic
    private Integer deleted;
}
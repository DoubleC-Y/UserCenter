-- =====================================================================
-- usercenter.`user` 建表脚本
-- 唯一 DDL 文件：后续所有表结构变更都直接改这里，不再写增量脚本
-- 目标环境：MySQL 8.0.46，InnoDB，utf8mb4 / utf8mb4_0900_ai_ci
-- 对应实体：com.example.background.model.domain.User（改这里必须同步改实体和 UserMapper.xml）
-- 注意事项：
--   1. id 为雪花 ID，由 MyBatis-Plus 的 IdType.ASSIGN_ID 在应用侧生成，
--      因此这里刻意不加 AUTO_INCREMENT
--   2. account 仅数字，由 CHECK 约束兜底；正则要用 [0-9]，MySQL 不支持 \d
--   3. password 存「16位十六进制盐 + $ + SHA-256摘要」，明文永不落库，
--      格式由 com.example.background.utils.PasswordUtil 定义
--   4. create_time / update_time 由数据库默认值维护，Java 侧只读：
--      插入时保持实体字段为 null，MyBatis-Plus 的 insert-strategy=not_null
--      会跳过 null 字段，默认值才能生效
--   5. deleted 配合 mybatis-plus.global-config.db-config.logic-delete-field 使用
--   6. role 用 tinyint 而不是 ENUM：0普通用户 / 1管理员，后续要加 2、3、4、5... 只改注释即可，
--      不动表结构；也因此刻意不加 CHECK 约束，免得每新增一种角色就要改一次 DDL
-- =====================================================================

-- 首次建库时按需放开：直接执行会清掉表现有数据
-- DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    `id`          bigint       NOT NULL COMMENT '主键ID',
    `name`        varchar(30)           DEFAULT NULL COMMENT '姓名',
    `account`     varchar(20)  NOT NULL COMMENT '登录账号:仅数字',
    `password`    varchar(100) NOT NULL COMMENT '密码:盐$SHA-256摘要',
    `age`         int                   DEFAULT NULL COMMENT '年龄',
    `email`       varchar(50)           DEFAULT NULL COMMENT '邮箱',
    `phone`       varchar(20)           DEFAULT NULL COMMENT '手机号',
    `avatar`      varchar(500)          DEFAULT NULL COMMENT '头像URL',
    `intro`       varchar(500)          DEFAULT NULL COMMENT '个人简介',
    `enabled`     tinyint      NOT NULL DEFAULT 1 COMMENT '是否启用:1启用,0停用',
    `work_status` tinyint      NOT NULL DEFAULT 1 COMMENT '人员状态:1在职,2离职',
    `role`        tinyint      NOT NULL DEFAULT 0 COMMENT '角色:0普通用户,1管理员',
    `create_time` datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time` datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `deleted`     tinyint      NOT NULL DEFAULT 0 COMMENT '逻辑删除:0未删除,1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_account` (`account`),
    KEY `idx_phone` (`phone`),
    CONSTRAINT `chk_user_account_digits` CHECK (`account` REGEXP '^[0-9]+$')
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


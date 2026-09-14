package com.example.background.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * 密码摘要工具：SHA-256 + 每个用户独立随机盐
 *
 * <p>存储格式为 {@code 16位十六进制盐 + "$" + 64位十六进制摘要}，共 81 个字符，
 * 与 user.password 的 varchar(100) 对应。摘要是单向的，数据库整库泄露也还原不出明文，
 * 因此任何时候都不要把它当作可逆加密来使用。</p>
 *
 * <p>校验只允许走 {@link #matches(String, String)}，不要自己拆字符串比对：
 * 该方法使用常量时间比较，且能正确处理盐与摘要的分隔。</p>
 *
 * @author 10611
 */
public final class PasswordUtil {

    /**
     * 盐的十六进制字符数，16 个 hex 字符对应 8 字节随机数
     */
    private static final int SALT_HEX_LENGTH = 16;

    /**
     * 盐与明文拼接后使用的摘要算法
     */
    private static final String DIGEST_ALGORITHM = "SHA-256";

    /**
     * 分隔符，与数据库里已有的存量数据保持一致
     */
    private static final char SEPARATOR = '$';

    /**
     * SecureRandom 本身线程安全，全局复用即可，避免每次调用重新种种子
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * HexFormat.of() 输出小写十六进制，与 MySQL SHA2() 的结果格式一致
     */
    private static final HexFormat HEX = HexFormat.of();

    private PasswordUtil() {
    }

    /**
     * 生成密码摘要，每次调用盐都不同，所以同一明文的密文也不同
     *
     * @param rawPassword 明文密码
     * @return 形如 {@code 44aa167514c8c233$394cc954...} 的摘要串
     */
    public static String encrypt(String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("密码不能为 null");
        }
        byte[] saltBytes = new byte[SALT_HEX_LENGTH / 2];
        SECURE_RANDOM.nextBytes(saltBytes);
        String salt = HEX.formatHex(saltBytes);
        return salt + SEPARATOR + digest(salt, rawPassword);
    }

    /**
     * 校验明文密码与库里的摘要串是否匹配
     *
     * @param rawPassword    待校验的明文密码
     * @param storedPassword 数据库里存的摘要串
     * @return 格式不合法（例如历史脏数据、空串）一律返回 false
     */
    public static boolean matches(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }
        int separatorIndex = storedPassword.indexOf(SEPARATOR);
        if (separatorIndex != SALT_HEX_LENGTH) {
            return false;
        }
        String salt = storedPassword.substring(0, separatorIndex);
        String storedDigest = storedPassword.substring(separatorIndex + 1);
        // 常量时间比较，避免因耗时差异推算出摘要内容
        return MessageDigest.isEqual(storedDigest.getBytes(StandardCharsets.UTF_8),
                digest(salt, rawPassword).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 摘要规则：SHA-256(盐 + 明文)，UTF-8 编码，小写十六进制输出
     * 迁移存量数据时数据库里用的是 SHA2(CONCAT(盐, 明文), 256)，两者必须完全对应
     */
    private static String digest(String salt, String rawPassword) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
            return HEX.formatHex(messageDigest.digest((salt + rawPassword).getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(DIGEST_ALGORITHM + " 算法不可用", e);
        }
    }
}

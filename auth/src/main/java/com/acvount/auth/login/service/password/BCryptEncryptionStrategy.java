package com.acvount.auth.login.service.password;

import cn.dev33.satoken.secure.BCrypt;
import com.acvount.auth.login.service.password.strategy.PasswordEncryptionStrategy;

/**
 * @author : acfan
 * date : create in 2023/7/9 20:46
 * description : BCrypt 加密实现
 **/

public class BCryptEncryptionStrategy implements PasswordEncryptionStrategy {

    private final static Integer SALT_LENGTH = 32;


    /***
     * @param password String 需要加密的密码
     * @return String[] 0:salt 1:hashedPassword
     */
    @Override
    public String[] encryptPassword(String password) {
        String salt = generSalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        return new String[]{salt, hashedPassword};
    }

    @Override
    public String getEncryptionType() {
        return "bcrypt";
    }

    @Override
    public Boolean checkPassword(String password, String salt, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    private String generSalt() {
        return BCrypt.gensalt(SALT_LENGTH);
    }
}

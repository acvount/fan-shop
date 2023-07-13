package com.acvount.auth.login.service.password.strategy;

import com.acvount.auth.login.service.password.BCryptEncryptionStrategy;
import com.acvount.auth.login.service.password.SHA512EncryptionStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author : acfan
 * date : create in 2023/7/9 20:45
 * description : 密码加密策略
 **/

public interface PasswordEncryptionStrategy {

    List<PasswordEncryptionStrategy> list = Arrays.asList(
            new BCryptEncryptionStrategy(),
            new SHA512EncryptionStrategy()
    );

    /***
     * @param password String 需要加密的密码
     * @return String[] 0:salt 1:hashedPassword
     */
    String[] encryptPassword(String password);

    String getEncryptionType();

    Boolean checkPassword(String password, String salt, String hashedPassword);


    static PasswordEncryptionStrategy getStrategy() {
        return list.get(new Random().nextInt(list.size()));
    }

    static PasswordEncryptionStrategy getStrategy(String type) {
        return list.stream().filter(e -> e.getEncryptionType().equals(type)).findFirst().orElse(null);
    }
}

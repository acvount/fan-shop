package com.acvount.auth.login.service.password;

import com.acvount.auth.login.service.password.strategy.PasswordEncryptionStrategy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author : acfan
 * date : create in 2023/7/9 21:16
 * description : SHA-512 算法加密
 **/

public class SHA512EncryptionStrategy implements PasswordEncryptionStrategy {


    private static final int SALT_LENGTH = 32;

    @Override
    public String[] encryptPassword(String password) {
        try {
            byte[] salt = generateSalt();
            MessageDigest md = MessageDigest.getInstance(getEncryptionType());
            md.update(salt);
            return new String[]{byteToHex(salt), byteToHex(md.digest(password.getBytes()))};
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] encryptPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(getEncryptionType());
            md.update(salt);
            return new String[]{byteToHex(salt), byteToHex(md.digest(password.getBytes()))};
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getEncryptionType() {
        return "SHA-512";
    }

    @Override
    public Boolean checkPassword(String password, String salt, String hashedPassword) {
        return encryptPassword(password, hexToByte(salt))[1].equals(hashedPassword);
    }

    private String byteToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private byte[] hexToByte(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];

        for (int i = 0; i < length; i += 2) {
            String hex = hexString.substring(i, i + 2);
            byte value = (byte) Integer.parseInt(hex, 16);
            byteArray[i / 2] = value;
        }

        return byteArray;
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
}
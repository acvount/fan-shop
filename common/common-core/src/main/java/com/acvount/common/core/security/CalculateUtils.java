package com.acvount.common.core.security;

import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author : acfan
 * date : create in 2023/7/22 10:27
 * description :
 **/

public class CalculateUtils {

    private final static String MD5_TYPE = "MD5";


    public static <T extends Serializable> String md5(T val) {
        return calculateByType(MD5_TYPE, val);
    }


    @SneakyThrows
    private static <T extends Serializable> byte[] toBytes(T obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
        return bos.toByteArray();
    }

    private static <T extends Serializable> String calculateByType(String type, T val) {
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            byte[] digest = md.digest(toBytes(val));

            // 将字节数组转换为16进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xFF));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return Strings.EMPTY;
        }
    }
}

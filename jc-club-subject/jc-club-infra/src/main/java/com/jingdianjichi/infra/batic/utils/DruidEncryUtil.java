package com.jingdianjichi.infra.batic.utils;

import com.alibaba.druid.filter.config.ConfigTools;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * 数据库加密Util
 */
public class DruidEncryUtil {

    private static String publicKey;

    private static String privateKey;

    static {
        try {
            String[] keyPair = ConfigTools.genKeyPair(512);
            privateKey = keyPair[0];
            System.out.println("privateKey" + privateKey);
            publicKey = keyPair[1];
            System.out.println("publicKey1" + publicKey);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String plainText) throws Exception {
        String encrypt = ConfigTools.encrypt(privateKey,plainText);
        return encrypt;
    }

    public static String decrypt(String encryptText) throws Exception {
        String decrypt = ConfigTools.decrypt(publicKey,encryptText);
        return decrypt;
    }

    public static void main(String[] args) throws Exception {
        String encrypt = encrypt("root");
        System.out.println(encrypt);
    }
}

package com.example.lib_equipment_management_system.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 用于加解密字符串，在这常用于将用户账号设置为USER_ID对应的cookie值
 */
@Slf4j
public class RSAUtils {

    private static String PRIVATE_KEY;
    private static String PUBLIC_KEY;

    private static XRsa RSA;

    static {
        Map<String, String> keys = XRsa.createKeys(528);
        PRIVATE_KEY = keys.get("privateKey");
        PUBLIC_KEY = keys.get("publicKey");
        RSA = new XRsa(PUBLIC_KEY, PRIVATE_KEY);
    }

    public static String encryptString(String str) {
        try {
            return RSA.publicEncrypt(str);
        }catch (Exception e) {
            log.error("", e);
            return "-1-";
        }

    }

    public static String decryptString(String str) {
        try {
            return RSA.privateDecrypt(str);
        } catch (Exception e) {
            log.error("",e);
            return "-1-";
        }

    }

}

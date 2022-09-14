package com.example.lib_equipment_management_system.utils;

import org.springframework.util.DigestUtils;

public class Encrypt {

    /**
     * 用于加密(签名)密码
     */
    public static String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

}

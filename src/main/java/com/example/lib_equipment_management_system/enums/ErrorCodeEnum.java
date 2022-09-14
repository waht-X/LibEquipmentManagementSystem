package com.example.lib_equipment_management_system.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ErrorCodeEnum {

    CORRECT(-1, "CORRECT"),
    OTHERS(0, "其他未知错误"),
    SERVER_ERROR(1, "服务器错误"),
    UN_LOGIN(2, "未登录"),
    USER_NOT_EXIST(3, "用户不存在"),
    ILLEGAL_ACCOUNT(4, "非法账号格式，账号只能由数字和字母组成"),
    ILLEGAL_PASSWORD(5, "非法密码格式，密码只能由数字和字母组成"),
    INCORRECT_ACCOUNT(6, "账号或密码不正确"),
    INCOMPLETE_USER_INFO(7, "用户信息不全"),
    USER_EXIST(8, "用户已存在"),
    INVALID_EQUIPMENT_ENTITY(9, "非法的设备表单数据"),
    PERMISSION_DENY(10, "权限不足"),
    INVALID_EMAIL(11, "非法邮箱"),
    INVALID_DATA(12, "非法数据");

    private int code;
    private String desc;

    @Setter
    private Object data;

     ErrorCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

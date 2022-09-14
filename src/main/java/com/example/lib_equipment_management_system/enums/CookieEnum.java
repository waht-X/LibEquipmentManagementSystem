package com.example.lib_equipment_management_system.enums;

public enum CookieEnum {

    USER_ID("USER_ID_token");

    private String value;

    CookieEnum(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return value;
    }
}

package com.example.lib_equipment_management_system.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表单状态,由枚举类管理，0提交状态，1表示通过状态，2表示驳回
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FormStatusEnum {

    SUBMIT(0, "已提交"),
    PASS(1, "已通过"),
    REJECTED(2, "已驳回");

    private byte id;
    private String desc;

    FormStatusEnum(int id, String desc) {
        this.id = (byte) id;
        this.desc = desc;
    }

    public static FormStatusEnum getById(int id) {
        for (FormStatusEnum value : FormStatusEnum.values()) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }

    /**
     * 判断该id的合法性，是否存在于该枚举类中
     * @return true如果合法,反之false
     */
    public static boolean checkId(int id) {
        return getById(id) != null;
    }


    public static List<FormStatusEnum> getAllFormStatus() {
        return Arrays.stream(FormStatusEnum.values()).collect(Collectors.toList());
    }
}

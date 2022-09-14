package com.example.lib_equipment_management_system.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于表示提交的工厂表单的类型
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum FormTypeEnum implements EnumEnhanceFun<FormTypeEnum> {

    SCRAP_FORM(1, "报废表单"),
    MAINTENANCE_FORM(2, "维修表单"),
    DEMAND_FORM(3, "申请表单");
    private byte id;
    private String desc;

    FormTypeEnum(int id, String desc) {
        this.id = (byte) id;
        this.desc = desc;
    }

    public static List<FormTypeEnum> getAllFormType() {
        return Arrays.stream(FormTypeEnum.values()).collect(Collectors.toList());
    }

    public static FormTypeEnum getById(int id) {
        for (FormTypeEnum value : FormTypeEnum.values()) {
            if ((int) value.id == id) {
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
}

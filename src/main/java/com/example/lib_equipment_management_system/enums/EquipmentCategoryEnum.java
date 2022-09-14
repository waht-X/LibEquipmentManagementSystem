package com.example.lib_equipment_management_system.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于设备的分类属性（category）枚举值
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EquipmentCategoryEnum implements EnumEnhanceFun<EquipmentCategoryEnum> {

    A((byte) 0, "A类"),
    B((byte) 1, "B类"),
    C((byte) 2, "C类"),
    D((byte) 3, "D类");

    /**
     * 分类码
     */
    private final byte id;
    /**
     * 对应分类的描述
     */
    private final String desc;

    EquipmentCategoryEnum(byte id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public static List<EquipmentCategoryEnum> getAllCategory() {
        return Arrays.stream(EquipmentCategoryEnum.values()).collect(Collectors.toList());
    }


    public static EquipmentCategoryEnum getById(int id) {
        for (EquipmentCategoryEnum value : EquipmentCategoryEnum.values()) {
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

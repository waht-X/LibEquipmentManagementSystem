package com.example.lib_equipment_management_system.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 表示数据状态的枚举类<br>
 * <code>NORMAL((byte) 0, "正常"),</code><br>
 * <code>ABANDON((byte) 1, "废弃/冻结/无操作"),</code><br>
 * <code>DELETE((byte) 2, "软删除");</code><br>
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusEnum implements EnumEnhanceFun<StatusEnum> {
    NORMAL((byte) 0, "正常"),
    ABANDON((byte) 1, "废弃/冻结"),
    DELETE((byte) 2, "软删除");

    /**
     * 状态码
     */
    private final byte id;

    /**
     * 状态码描述
     */
    private final String desc;

    StatusEnum(byte id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public static StatusEnum getById(int id) {
        for (StatusEnum value : StatusEnum.values()) {
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

    public static List<StatusEnum> getAllStatusEnum() {
        return Arrays.stream(StatusEnum.values()).collect(Collectors.toList());
    }
}

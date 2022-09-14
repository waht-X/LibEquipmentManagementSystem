package com.example.lib_equipment_management_system.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门枚举管理
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DepartmentEnum {

    A(1, "A部门"),
    B(2, "B部门"),
    C(3, "C部门"),
    D(4, "D部门"),
    E(5, "E部门");

    private int id;
    private String desc;

    DepartmentEnum(int id, String desc) {
        this.id = (byte) id;
        this.desc = desc;
    }

    public static List<DepartmentEnum> getAllDepartmentEnum() {
        return Arrays.stream(DepartmentEnum.values()).collect(Collectors.toList());
    }

    public static DepartmentEnum getById(int id) {
        for (DepartmentEnum value : DepartmentEnum.values()) {
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

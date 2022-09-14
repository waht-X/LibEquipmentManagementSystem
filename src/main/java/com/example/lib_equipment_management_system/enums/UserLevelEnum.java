package com.example.lib_equipment_management_system.enums;

import lombok.Getter;

/**
 * 枚举管理用户等级<br>
 * 0级为最普通的等级,具备最基本的表单提交权限和查看自生表单记录的权限<br>
 * 如果用户等级为最低级的0级,则只能查看自身的表单记录<br>
 * 如果大于0级，则能查看小于等于自身等级的记录<br>
 * 只有等级大于3的账户具有处理表单权限
 */
@Getter
public enum UserLevelEnum {

    LEVEL0(0, "等级0"),
    LEVEL1(1, "等级1"),
    LEVEL2(2, "等级2"),
    LEVEL3(3, "等级3"),
    LEVEL4(4, "等级4"),
    LEVEL5(5, "等级5");

    private byte id;
    private String desc;

    UserLevelEnum(int id, String desc) {
        this.id = (byte) id;
        this.desc = desc;
    }

    /**
     * 判断是否是具有处理表单的等级<br>
     * 只有等级大于3的账户具有处理表单权限
     * @return 具备权限返回true
     */
    public static boolean isHandler(int level) {
        return level >= 3;
    }

    /**
     * superHandler具备操作账户的权力
     */
    public static boolean isSuperHandler(int level) {
        return level >= 5;
    }

    /**
     * 判断该level是否合法存在
     */
    public static boolean exist(int level) {
        for (UserLevelEnum value : UserLevelEnum.values()) {
            if (value.id == level) {
                return true;
            }
        }
        return false;
    }

}

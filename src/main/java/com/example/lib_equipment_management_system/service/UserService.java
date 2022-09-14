package com.example.lib_equipment_management_system.service;

import com.example.lib_equipment_management_system.entity.EquipmentUserEntity;
import com.example.lib_equipment_management_system.enums.ErrorCodeEnum;

public interface UserService {

    ErrorCodeEnum modifyUserLevel(int model, String account);

    ErrorCodeEnum deleteUser(String account);

    EquipmentUserEntity modifyUserInfo(EquipmentUserEntity user);
    ErrorCodeEnum userInfo(String account);

    ErrorCodeEnum login(EquipmentUserEntity user);

    ErrorCodeEnum register(EquipmentUserEntity user);

    ErrorCodeEnum getAllUsersInfo(int size, int index);

}

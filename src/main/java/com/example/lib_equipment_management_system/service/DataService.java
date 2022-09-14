package com.example.lib_equipment_management_system.service;


import com.example.lib_equipment_management_system.entity.EquipmentFormEntity;
import com.example.lib_equipment_management_system.enums.ErrorCodeEnum;
import com.example.lib_equipment_management_system.request.ReqSearchDO;
import org.springframework.data.domain.Page;


public interface DataService {

    ErrorCodeEnum save(EquipmentFormEntity entity);

    Page<EquipmentFormEntity> search(ReqSearchDO searchDO);

    ErrorCodeEnum update(EquipmentFormEntity entity);

}

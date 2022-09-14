package com.example.lib_equipment_management_system.controller;

import com.example.lib_equipment_management_system.entity.EquipmentFormEntity;
import com.example.lib_equipment_management_system.enums.*;
import com.example.lib_equipment_management_system.request.ReqSearchDO;
import com.example.lib_equipment_management_system.response.ResponseVO;
import com.example.lib_equipment_management_system.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("data")
public class DataController {

    @Resource
    DataService dataService;

    @PostMapping("submitForm")
    public ResponseVO submitForm(@RequestBody EquipmentFormEntity equipmentEntity) {
        log.debug("submit data [{}], FormType= {}", equipmentEntity, FormTypeEnum.getById(equipmentEntity.getFormType()));
        ErrorCodeEnum errorCode = dataService.save(equipmentEntity);
        if (errorCode == ErrorCodeEnum.CORRECT) {
            return new ResponseVO(true, null, errorCode.getDesc(), errorCode.getCode());
        } else {
            return new ResponseVO(false, null, errorCode.getDesc(), errorCode.getCode());
        }
    }

    @RequestMapping("allCategories")
    public ResponseVO getAllCategories() {
        log.debug("getAllCategories");
        return new ResponseVO(true, EquipmentCategoryEnum.getAllCategory(),
                ErrorCodeEnum.CORRECT.getDesc(), ErrorCodeEnum.CORRECT.getCode());
    }

    @RequestMapping("allDepartment")
    public ResponseVO getAllDepartment() {
        log.debug("getAllDepartment");
        return new ResponseVO(true, DepartmentEnum.getAllDepartmentEnum(),
                ErrorCodeEnum.CORRECT.getDesc(), ErrorCodeEnum.CORRECT.getCode());
    }

    @RequestMapping("allFormType")
    public ResponseVO getAllFormType() {
        log.debug("getAllFormType");
        return new ResponseVO(true, FormTypeEnum.getAllFormType(),
                ErrorCodeEnum.CORRECT.getDesc(), ErrorCodeEnum.CORRECT.getCode());
    }

    @RequestMapping("allFormStatus")
    public ResponseVO getAllFormStatus() {
        log.debug("getAllFormStatus");
        return new ResponseVO(true, FormStatusEnum.getAllFormStatus(),
                ErrorCodeEnum.CORRECT.getDesc(), ErrorCodeEnum.CORRECT.getCode());
    }

    @RequestMapping("getAllStatus")
    public ResponseVO getAllStatus() {
        log.debug("getAllStatus");
        return new ResponseVO(true, StatusEnum.getAllStatusEnum(),
                ErrorCodeEnum.CORRECT.getDesc(), ErrorCodeEnum.CORRECT.getCode());
    }

    @PostMapping("search")
    public ResponseVO search(@RequestBody ReqSearchDO reqSearchDO) {
        Page<EquipmentFormEntity> searchResult = dataService.search(reqSearchDO);
        HashMap<String, Object> data = new HashMap<>();
        if (searchResult == null) {
            return new ResponseVO(false, data, ErrorCodeEnum.OTHERS.getDesc(), ErrorCodeEnum.OTHERS.getCode());
        } else {
            data.put("searchResult", searchResult.getContent());
            data.put("totalPage", searchResult.getTotalPages());
            return new ResponseVO(true, data, ErrorCodeEnum.CORRECT.getDesc(), ErrorCodeEnum.CORRECT.getCode());
        }
    }

    @PostMapping("update")
    public ResponseVO update(@RequestBody EquipmentFormEntity entity) {
        ErrorCodeEnum errorCode = dataService.update(entity);
        if (errorCode != ErrorCodeEnum.CORRECT) {
            return new ResponseVO(false, null, errorCode.getDesc(), errorCode.getCode());
        }
        return new ResponseVO(true, null, errorCode.getDesc(), errorCode.getCode());
    }
}

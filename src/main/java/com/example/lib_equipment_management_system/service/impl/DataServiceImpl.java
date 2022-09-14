package com.example.lib_equipment_management_system.service.impl;

import com.example.lib_equipment_management_system.Exception.NoSuchCookieNameException;
import com.example.lib_equipment_management_system.entity.EquipmentFormEntity;
import com.example.lib_equipment_management_system.entity.EquipmentUserEntity;
import com.example.lib_equipment_management_system.enums.*;
import com.example.lib_equipment_management_system.repository.EquipmentEntityRepository;
import com.example.lib_equipment_management_system.repository.UserRepository;
import com.example.lib_equipment_management_system.request.ReqSearchDO;
import com.example.lib_equipment_management_system.service.DataService;
import com.example.lib_equipment_management_system.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DataServiceImpl implements DataService {

    @Resource
    EquipmentEntityRepository equipmentEntityRepository;

    @Resource
    UserRepository userRepository;

    @Resource
    HttpServletRequest request;

    @Override
    public ErrorCodeEnum save(EquipmentFormEntity entity) {
        try {
            String account = getAccount(request);
            if (userRepository.existsByAccount(account)) {
                //判断该实体类的合法性
                if (!isValid(entity)) {
                    return ErrorCodeEnum.INVALID_EQUIPMENT_ENTITY;
                }
                EquipmentUserEntity user = userRepository.findIdByAccount(account);
                entity.setUserId(user.getId());
                entity.setStatus(StatusEnum.NORMAL.getId());
                entity.setAddTime(LocalDateTime.now());
                entity.setModifyTime(LocalDateTime.now());
                entity.setLevel(user.getLevel());
                if (entity.getSerial() == null) {
                    entity.setSerial(UUID.randomUUID().toString());
                }
                if (entity.getFormStatus() == null) {
                    entity.setFormStatus(FormStatusEnum.SUBMIT.getId());
                }
                equipmentEntityRepository.save(entity);
                log.info("[数据插入] ==> [插入的数据: {}, 操作者账户: {}]", entity, user.getAccount());
                return ErrorCodeEnum.CORRECT;
            } else {
                log.warn("拦截器未生效或出现错误，未成功拦截未登录的请求，该account值:[{}]", account);
                return ErrorCodeEnum.USER_NOT_EXIST;
            }
        } catch (NoSuchCookieNameException e) {
            log.warn("拦截器未生效或出现错误，未成功拦截无 CookieEnum.USER_ID 值的请求", e);
            return ErrorCodeEnum.UN_LOGIN;
        }
    }



    @Override
    public Page<EquipmentFormEntity> search(ReqSearchDO searchDO) {
        log.debug("搜索的searchDO = {}", searchDO);
        EquipmentUserEntity user = null;
        try {
            //获取用户账户消息
            String account = getAccount(request);
            user = userRepository.findByAccount(account);
        } catch (NoSuchCookieNameException e) {
            log.warn("将cookie解密为用户账号出现问题, 将直接返回null数据", e);
            return null;
        }

        EquipmentUserEntity finalUser = user;
        Page<EquipmentFormEntity> all = equipmentEntityRepository.findAll((Specification<EquipmentFormEntity>)
                (root, query, cr) -> {
                    List<Predicate> predicateList = new ArrayList<>();
                    Assert.notNull(finalUser.getLevel(), "LeveL param is null,but it must be not null, please check the reason of it");
                    if (finalUser.getLevel() == UserLevelEnum.LEVEL0.getId()) {
                        //如果用户等级为最低级的0级,则只能查看自身的表单记录
                        predicateList.add(cr.equal(root.get("userId"), finalUser.getId()));
                    } else {
                        //如果大于0级，则能查看小于等于自身等级的记录
                        predicateList.add(cr.le(root.get("level"), finalUser.getLevel()));
                    }
                    predicateList.add(cr.equal(root.get("status"), StatusEnum.NORMAL.getId()));
                    if (searchDO.getName() != null) {
                        predicateList.add(cr.equal(root.get("name"), searchDO.getName()));
                    }
                    if (searchDO.getCategory() != null) {
                        predicateList.add(cr.equal(root.get("category"), searchDO.getCategory()));
                    }
                    if (searchDO.getDepartment() != null) {
                        predicateList.add(cr.equal(root.get("department"), searchDO.getDepartment()));
                    }
                    if (searchDO.getFormType() != null) {
                        predicateList.add(cr.equal(root.get("formType"), searchDO.getFormType()));
                    }
                    if (searchDO.getStartTime() != null) {
                        predicateList.add(cr.between(root.get("addTime"), searchDO.getStartTime(), searchDO.getEndTime()));
                    }
                    if (searchDO.getFormStatus() != null) {
                        predicateList.add(cr.equal(root.get("formStatus"), searchDO.getFormStatus()));
                    }
                    predicateList.add(cr.equal(root.get("status"), StatusEnum.NORMAL.getId()));

                    return query.where(predicateList.toArray(new Predicate[0])).getRestriction();
                }, PageRequest.of(searchDO.getPageIndex(), searchDO.getPageSize(), Sort.by("id")));
        return all;
    }

    @Override
    public ErrorCodeEnum update(EquipmentFormEntity entity) {
        //判断表单修改人是否合法
        try {
            String account = getAccount(request);
            //判断修改人账户等级
            EquipmentUserEntity user = userRepository.findByAccount(account);
            Assert.notNull(user.getLevel(), "用户权限为空");
            if (!UserLevelEnum.isHandler(user.getLevel().intValue())) {
                //权限不足
                return ErrorCodeEnum.PERMISSION_DENY;
            }
            if (entity.getId() == 0) {
                log.info("需要进行更新的数据的id=[{}], 不是数据库中正常的数据, 系统将拒绝插入", entity.getId());
                log.info("拒绝插入的数据: [{}]", entity);
                return ErrorCodeEnum.INVALID_EQUIPMENT_ENTITY;
            }
            //更新数据
            if (!isValid(entity) || !FormStatusEnum.checkId(entity.getFormStatus()) ) {
                return ErrorCodeEnum.INVALID_EQUIPMENT_ENTITY;
            }

            entity.setModifyTime(LocalDateTime.now());
            entity.setHandlerId(user.getId());
            equipmentEntityRepository.save(entity);
            log.info("[数据更新]==> [更新的数据实体: {}, 更新者账户: {}]", entity, user.getAccount());
        } catch (NoSuchCookieNameException e) {
            log.warn("更新表单时，将cookie解密为用户账号出现问题, 将直接返回null数据", e);
            return ErrorCodeEnum.SERVER_ERROR;
        }
        return ErrorCodeEnum.CORRECT;
    }

    private String getAccount(HttpServletRequest request) throws NoSuchCookieNameException {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(CookieEnum.USER_ID.toString())) {
                return RSAUtils.decryptString(cookie.getValue());
            }
        }
        throw new NoSuchCookieNameException();
    }

    /**
     * @return 合法返回true，反之false
     */
    private boolean isValid(EquipmentFormEntity entity) {
        //判断formType字段的合法性
        if (!FormTypeEnum.checkId(entity.getFormType())) {
            log.info("该数据的formType[formType = {}]字段不合法", entity.getFormType());
            return false;
        }
        //判断category字段的合法性
        if (entity.getCategory() == null || !EquipmentCategoryEnum.checkId(entity.getCategory())) {
            log.info("该数据的category[{}]字段不合法", entity.getCategory());
            return false;
        }
        //判断department字段的合法性
        if (!DepartmentEnum.checkId(entity.getDepartment())) {
            log.info("该数据的department[{}]字段不合法", entity.getDepartment());
            return false;
        }
        return true;
    }

}

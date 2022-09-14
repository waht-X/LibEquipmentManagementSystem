package com.example.lib_equipment_management_system.service.impl;

import com.example.lib_equipment_management_system.Exception.NoSuchCookieNameException;
import com.example.lib_equipment_management_system.entity.EquipmentUserEntity;
import com.example.lib_equipment_management_system.enums.CookieEnum;
import com.example.lib_equipment_management_system.enums.ErrorCodeEnum;
import com.example.lib_equipment_management_system.enums.StatusEnum;
import com.example.lib_equipment_management_system.enums.UserLevelEnum;
import com.example.lib_equipment_management_system.repository.UserRepository;
import com.example.lib_equipment_management_system.service.UserService;
import com.example.lib_equipment_management_system.utils.Encrypt;
import com.example.lib_equipment_management_system.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository userRepository;

    @Resource
    HttpServletResponse response;

    @Resource
    HttpServletRequest request;

    @Override
    public ErrorCodeEnum modifyUserLevel(int model, String account) {
        String operatorAcc = null;
        try {
            operatorAcc = getAccount(request);
        } catch (NoSuchCookieNameException e) {
            log.warn("拦截器错误", e);
            return ErrorCodeEnum.SERVER_ERROR;
        }
        EquipmentUserEntity operator = userRepository.findByAccount(operatorAcc);
        EquipmentUserEntity user = userRepository.findByAccount(account);
        if (UserLevelEnum.isSuperHandler(user.getLevel()) || !UserLevelEnum.isSuperHandler(operator.getLevel())) {
            log.info("用户[{}]尝试越权操作,被修改用户[account={}], 拒绝", operator, account);
            return ErrorCodeEnum.PERMISSION_DENY;
        }
        int level = user.getLevel();
        if (model > 0) {
            //权限增加
            level++;
            if (UserLevelEnum.isSuperHandler(level)) {
                //不允许直接通过这种方式增加到超级用户
                log.info("用户[account={}]尝试成为超级用户SuperHandler[operator=>{}]", account, operator);
                return ErrorCodeEnum.PERMISSION_DENY;
            }
        } else {
            level--;
        }
        if (!UserLevelEnum.exist(level)) {
            log.warn("不合法的level[level={}]", level);
            return ErrorCodeEnum.INVALID_DATA;
        }
        user.setLevel((byte) level);
        user.setModifyTime(LocalDateTime.now());
        userRepository.save(user);
        return ErrorCodeEnum.CORRECT;
    }

    @Override
    public ErrorCodeEnum deleteUser(String account) {
        log.info("将对账户[account={}]进行删除操作", account);
        String operatorAcc = null;
        try {
            operatorAcc = getAccount(request);
        } catch (NoSuchCookieNameException e) {
            log.warn("",e);
            return ErrorCodeEnum.SERVER_ERROR;
        }
        EquipmentUserEntity operator = userRepository.findByAccount(operatorAcc);
        if (!UserLevelEnum.isSuperHandler(operator.getLevel())) {
            log.warn("违法操作，权限不足，进行操作的账户[{}],被操纵的账户[{}]", operator, account);
            return ErrorCodeEnum.PERMISSION_DENY;
        }
        EquipmentUserEntity user = userRepository.findByAccount(account);
        if (user == null) {
            return ErrorCodeEnum.USER_NOT_EXIST;
        }
        user.setStatus(StatusEnum.DELETE.getId());
        user.setModifyTime(LocalDateTime.now());
        userRepository.save(user);
        return ErrorCodeEnum.CORRECT;

    }

    @Override
    public EquipmentUserEntity modifyUserInfo(EquipmentUserEntity user) {
        log.debug("user[{}] will modify info", user.getAccount());
        if (user.getUsername() == null) {
            user.setUsername(UUID.randomUUID().toString());
        }
        user.setModifyTime(LocalDateTime.now());
        log.debug("modified info: [{}]", user);
        return userRepository.save(user);
    }

    @Override
    public ErrorCodeEnum userInfo(String account) {
        log.debug("获取用户消息userInfo[account={}]", account);
        EquipmentUserEntity byAccount = userRepository.findByAccount(account);
        log.debug("获取到的用户消息: [{}]", byAccount);
        byAccount.setPassword(null);
        byAccount.setModifyTime(null);
        byAccount.setAddTime(null);
        ErrorCodeEnum errorCode = ErrorCodeEnum.CORRECT;
        errorCode.setData(byAccount);
        return errorCode;
    }


    @Override
    public ErrorCodeEnum login(EquipmentUserEntity user) {
        log.info("user [{}] login", user.getAccount());
        EquipmentUserEntity userEntity = userRepository.findByAccount(user.getAccount());
        if (userEntity != null && userEntity.getPassword().equals(Encrypt.encryptPassword(user.getPassword()))) {
            //登录成功，发放token到cookie中
            Cookie cookie = new Cookie(CookieEnum.USER_ID.toString(),
                    RSAUtils.encryptString(user.getAccount()));
            cookie.setPath("/");
            response.addCookie(cookie);
            log.debug("发放token[cookieName={}, cookieValue={}] 给用户[{}]"
                    , cookie.getName(), cookie.getValue(), user.getAccount());
            return ErrorCodeEnum.CORRECT;
        }
        log.info("user [{}] account error, {}", user.getAccount(), ErrorCodeEnum.INCORRECT_ACCOUNT.getDesc());
        return ErrorCodeEnum.INCORRECT_ACCOUNT;
    }


    @Override
    public ErrorCodeEnum register(EquipmentUserEntity user) {
        log.debug("{} will register", user);
        if (!checkEmail(user.getEmail())) {
            return ErrorCodeEnum.INVALID_EMAIL;
        }
        if (!checkAccount(user.getAccount())) {
            return ErrorCodeEnum.ILLEGAL_ACCOUNT;
        }
        if (!checkPassword(user.getPassword())) {
            return ErrorCodeEnum.ILLEGAL_PASSWORD;
        }
        if (userRepository.existsByAccount(user.getAccount())) {
            return ErrorCodeEnum.USER_EXIST;
        }
        user.setPassword(Encrypt.encryptPassword(user.getPassword()));
        user.setAddTime(LocalDateTime.now());
        user.setModifyTime(LocalDateTime.now());
        user.setUsername(UUID.randomUUID().toString().substring(0, 10));
        userRepository.save(user);
        return ErrorCodeEnum.CORRECT;
    }

    @Override
    public ErrorCodeEnum getAllUsersInfo(int size, int index) {
        String account = null;
        try {
            account = getAccount(request);
        } catch (NoSuchCookieNameException e) {
            log.warn("获取用户信息解析request的cookie时出现问题，推测是拦截器未生效，将直接返回空值，Details:", e);
            return ErrorCodeEnum.SERVER_ERROR;
        }
        EquipmentUserEntity operator = userRepository.findByAccount(account);
        if (!UserLevelEnum.isSuperHandler(operator.getLevel())) {
            log.info("用户[{}]尝试越权操作，拒绝", operator);
            return ErrorCodeEnum.PERMISSION_DENY;
        }
        log.debug("操作者等级: [level={}]", operator.getLevel());
        Page<EquipmentUserEntity> all = userRepository.findAll(
                (Specification<EquipmentUserEntity>) (root, query, criteriaBuilder) -> {
                    //获取该账户等级下的所有账户
                    List<Predicate> predicateList = new ArrayList<>();
                    predicateList.add(criteriaBuilder.lessThan(root.get("level"), operator.getLevel()));
                    //筛选除删除的所有账户
                    predicateList.add(criteriaBuilder.notEqual(root.get("status"), StatusEnum.DELETE.getId()));
                    return query.where(predicateList.toArray(new Predicate[0])).getRestriction();
                }, PageRequest.of(index, size));
        //替换敏感字段
        for (EquipmentUserEntity equipmentUserEntity : all.getContent()) {
            equipmentUserEntity.setPassword(null);
            equipmentUserEntity.setId(0);
            String email = equipmentUserEntity.getEmail();
            equipmentUserEntity.setEmail(email.substring(0, 3) + "***" + email.substring(email.length() - 3));
        }
        log.debug("查询到的所有用户数据: [{}]", all.getContent());
        ErrorCodeEnum errorCode = ErrorCodeEnum.CORRECT;
        errorCode.setData(all);
        return errorCode;
    }


    /**
     * 保证account的规范
     * 1. 必须全部由字母或数字组成
     *
     * @return 如果符合条件返回true
     */
    boolean checkAccount(String account) {
        if (account.matches("([a-z]|\\d|[A-Z])+")) {
            return true;
        }
        return false;
    }

    boolean checkPassword(String password) {
        if (password.matches("([a-z]|\\d|[A-Z])+")) {
            return true;
        }
        return false;
    }

    boolean checkEmail(String email) {
        if (email == null || email.length() < 10) {
            return  false;
        }
        return true;
    }

    private String getAccount(HttpServletRequest request) throws NoSuchCookieNameException {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(CookieEnum.USER_ID.toString())) {
                log.debug("\ncookie[{}={}]", CookieEnum.USER_ID, cookie.getValue());
                return RSAUtils.decryptString(cookie.getValue());
            }
        }
        throw new NoSuchCookieNameException();
    }
}
